package ruxing.demo.entity.po.dto;

import lombok.Data;
import ruxing.demo.core.SearchConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruxing on 23/02/2017.
 */
@Data
public class SearchCondition {

    /**
     * 索引
     */
    private String index = SearchConstant.INDEX;

    /**
     * 类型
     */
    private String type = SearchConstant.TYPE;

    /**
     * 查询关键字
     */
    private String queryText;

    /**
     * 查询关键字指定字段
     */
    private List<String> queryTextField = new ArrayList<>();

    /**
     * match映射
     */
    private Map<String, Object> matchMap = new HashMap<>();

    /**
     * term映射
     */
    private Map<String, Object> termMap = new HashMap<>();

    /**
     * 是否有explain
     */
    private Boolean explain = false;

}
