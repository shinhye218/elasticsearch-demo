package ruxing.demo.service;

import ruxing.demo.entity.po.dto.SearchCondition;

import java.util.List;

/**
 * Created by ruxing on 20/02/2017.
 */
public interface SearchService {

    <T> List<T> commonSearch(SearchCondition searchCondition, Class clazz);

}
