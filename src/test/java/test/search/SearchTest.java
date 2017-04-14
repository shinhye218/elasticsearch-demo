package test.search;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ruxing.demo.core.ClientManager;
import ruxing.demo.core.SearchConstant;
import ruxing.demo.entity.po.User;
import ruxing.demo.entity.po.dto.SearchCondition;
import ruxing.demo.service.SearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruxing on 18/02/2017.
 */
@ContextConfiguration(locations = { "classpath*:/test/spring.xml"})
public class SearchTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ClientManager clientManager;

    @Autowired
    SearchService searchService;

    @Test
    public void commonSearch() {
        SearchCondition condition = new SearchCondition();
        condition.setQueryText("yuka seoul");
        List<String> textFieldList = new ArrayList<>();
        textFieldList.add("city");
        textFieldList.add("name");
        condition.setQueryTextField(textFieldList);

        Map<String, Object> matchMap = new HashMap<>();
        matchMap.put("name", "boa");
        matchMap.put("city", "Seoul");
        condition.setMatchMap(matchMap);

        Map<String, Object> termMap = new HashMap<>();
        termMap.put("name", "boa");
        condition.setTermMap(termMap);

        List<User> userList = searchService.commonSearch(condition, User.class);
        for (User user : userList) {
            System.out.println("user_id: " + user.getUserId());
            System.out.println("user_name: " + user.getName());
            System.out.println("user_age: " + user.getAge());
            System.out.println("user_city: " + user.getCity());
            System.out.println("--------------------------------");
        }
    }

    @Test
    public void multipleMatch(){
        Client client = clientManager.getClient();
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("yuka seoul", "name", "city");
        queryBuilder.operator(Operator.OR);

        SearchRequestBuilder builder = client.prepareSearch(SearchConstant.INDEX)
                .setTypes(SearchConstant.TYPE)
                .setQuery(queryBuilder);

        SearchResponse response = builder.get();
        SearchHits searchHits = response.getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.sourceAsString());
        }
    }

    /**
     * 聚合
     */
    @Test
    public void aggregationTest(){
        Client client = clientManager.getClient();

        //metrics aggregation下面不应该再跟subAggregation,取不出来。
        TopHitsAggregationBuilder topHits = AggregationBuilders.topHits("topHitsAgg").size(3).fetchSource("hometown", "");

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("ageAgg").field("age");
        termsAggregationBuilder.subAggregation(topHits);

        QueryBuilder termQueryBuilder = QueryBuilders.matchQuery("city", "Seoul, HongKong");
        FilterAggregationBuilder filterAggregationBuilder = AggregationBuilders.filter("filterAgg", termQueryBuilder);
        filterAggregationBuilder.subAggregation(termsAggregationBuilder);

        SearchRequestBuilder builder = client.prepareSearch(SearchConstant.INDEX)
                .setTypes(SearchConstant.TYPE)
                .addAggregation(filterAggregationBuilder);

        SearchResponse response = builder.get();
        Filter filter = response.getAggregations().get("filterAgg");
        Terms terms = filter.getAggregations().get("ageAgg");
        for (Terms.Bucket bucket: terms.getBuckets()) {
            TopHits topHitsAgg = bucket.getAggregations().get("topHitsAgg");
            SearchHit[] searchHits = topHitsAgg.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                System.out.println(searchHit.sourceAsString());
            }
        }
    }

}
