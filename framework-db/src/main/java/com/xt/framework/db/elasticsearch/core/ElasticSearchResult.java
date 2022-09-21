package com.xt.framework.db.elasticsearch.core;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author tao.xiong
 * @Description Es 查询结果返回
 * @Date 2022/9/19 15:18
 */
@Data
public class ElasticSearchResult<T> {
    private boolean success = true;
    private String message = "ok";
    private long totalHits;
    private String scrollId;
    private List<T> docList;
    private Map<String, Long> aggCountMap;

    public static <T> ElasticSearchResult<T> error(String message) {
        ElasticSearchResult<T> elasticSearchResult = new ElasticSearchResult<>();
        elasticSearchResult.setSuccess(false);
        elasticSearchResult.setMessage(message);
        return elasticSearchResult;
    }

    public static <T> ElasticSearchResult<T> success() {
        return new ElasticSearchResult<>();
    }
}
