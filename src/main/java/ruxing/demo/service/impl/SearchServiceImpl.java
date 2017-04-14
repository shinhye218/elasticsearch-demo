package ruxing.demo.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ruxing.demo.core.ClientManager;
import ruxing.demo.entity.po.dto.SearchCondition;
import ruxing.demo.service.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruxing on 20/02/2017.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ClientManager clientManager;

    public List<Object> commonSearch(SearchCondition condition, Class clazz) {
        if (condition == null) {
            return null;
        }
        List<Object> resultList = new ArrayList<>();

        QueryStringQueryBuilder queryStringQueryBuilder;
        MatchQueryBuilder matchQueryBuilder;
        TermQueryBuilder termQueryBuilder;

        Client client = clientManager.getClient();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(condition.getQueryText())) {
            queryStringQueryBuilder = QueryBuilders.queryStringQuery(condition.getQueryText());
            condition.getQueryTextField().forEach(queryStringQueryBuilder::field);
            queryBuilder.must(queryStringQueryBuilder);
        }

        for (String key : condition.getMatchMap().keySet()) {
            matchQueryBuilder = QueryBuilders.matchQuery(key, condition.getMatchMap().get(key));
            queryBuilder.must(matchQueryBuilder);
        }

        for (String key : condition.getTermMap().keySet()) {
            termQueryBuilder = QueryBuilders.termQuery(key, condition.getTermMap().get(key));
            queryBuilder.must(termQueryBuilder);
        }

        SearchRequestBuilder builder = client.prepareSearch(condition.getIndex()).setTypes(condition.getType())
                .setQuery(queryBuilder)
                .setExplain(condition.getExplain());

        SearchResponse response = builder.get();
        SearchHits searchHits = response.getHits();
        for (SearchHit hit : searchHits) {
            Object object = JSON.parseObject(hit.sourceAsString(), clazz);
            resultList.add(object);
        }
        return resultList;
    }

}
