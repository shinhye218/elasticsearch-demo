package ruxing.demo.service.impl;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ruxing.demo.core.ClientManager;
import ruxing.demo.core.SearchConstant;
import ruxing.demo.entity.po.User;
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

    public List<User> searchDemo() {

        List<User> userList = new ArrayList<User>();

        Client client = clientManager.getClient();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("city", "Seoul");
//        queryBuilder.must(matchQueryBuilder);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("city", "seoul");
        queryBuilder.must(termQueryBuilder);
        SearchRequestBuilder builder = client.prepareSearch(SearchConstant.INDEX).setTypes(SearchConstant.TYPE).setQuery(queryBuilder);
        SearchResponse response = builder.get();
        SearchHits searchHits = response.getHits();
        for (SearchHit hit : searchHits) {
            User user = JSON.parseObject(hit.sourceAsString(), User.class);
            userList.add(user);
        }
        return userList;
    }

}
