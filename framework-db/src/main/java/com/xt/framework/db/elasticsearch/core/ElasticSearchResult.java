package com.xt.framework.db.elasticsearch.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate.AGG_CARDINALITY;
import static com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate.AGG_TERM;

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
    private List<Object> resultList;
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

    public static <T> ElasticSearchResult<T> getResult(ElasticSearchRequest<T> elasticSearchRequest, SearchHits<T> searchHits) {
        if (elasticSearchRequest.getAggReq() != null) {
            Aggregations aggregations = searchHits.getAggregations();
            if (aggregations == null) {
                return ElasticSearchResult.error("处理聚合错误，聚合结果为空！");
            }
            Map<String, Long> resultMap = Maps.newConcurrentMap();
            Terms terms = aggregations.get(AGG_TERM);

            terms.getBuckets().stream().filter(buck -> !StringUtils.isBlank(buck.getKeyAsString())).forEach(buck -> {
                Aggregations buckAggregations = buck.getAggregations();
                Aggregation aggCardinality = buckAggregations.get(AGG_CARDINALITY);
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(aggCardinality));
                String cardinalityValue = jsonObject.getString("value");
                long count = Long.parseLong(cardinalityValue);
                Long minCount = elasticSearchRequest.getAggReq().getMinCount();
                if (minCount != null && count < minCount) {
                    return;
                }
                Long maxCount = elasticSearchRequest.getAggReq().getMaxCount();
                if (maxCount != null && count > maxCount) {
                    return;
                }
                resultMap.put(buck.getKeyAsString(), count);
            });
            ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
            elasticSearchResult.setAggCountMap(resultMap);
            return elasticSearchResult;
        }
        ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
        if (searchHits instanceof SearchScrollHits) {
            SearchScrollHits<T> scrollHits = (SearchScrollHits<T>) searchHits;
            elasticSearchResult.setScrollId(scrollHits.getScrollId());
        }
        List<T> list = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        elasticSearchResult.setTotalHits(searchHits.getTotalHits());
        List<Object> resultList = process(elasticSearchRequest, list);
        if (CollectionUtils.isEmpty(resultList)) {
            elasticSearchResult.setDocList(list);
        } else {
            elasticSearchResult.setResultList(resultList);
        }
        return elasticSearchResult;
    }

    public static <T> List<Object> process(ElasticSearchRequest<T> elasticSearchRequest, List<T> list) {
        List<Object> resultList = new ArrayList<>();
        if (elasticSearchRequest.getProcessReq() != null) {
            Function<List<T>, Object> function = elasticSearchRequest.getProcessReq().getFunction();
            Consumer<List<T>> consumer = elasticSearchRequest.getProcessReq().getConsumer();
            if (function != null) {
                resultList.add(function.apply(list));
            } else if (consumer != null) {
                consumer.accept(list);
            }
        } else {
            return Lists.newArrayList();
        }

        return resultList;
    }

    public static <T> ElasticSearchResult<T> getResult(ElasticSearchRequest<T> elasticSearchRequest, SearchHitsIterator<T> stream) {
        try {
            List<Object> resultList = new ArrayList<>();
            List<T> list = new ArrayList<>();
            while (stream.hasNext()) {
                T t = stream.next().getContent();
                List<T> l = new ArrayList<>();
                l.add(t);
                List<Object> results = process(elasticSearchRequest, l);
                if (!CollectionUtils.isEmpty(results)) {
                    resultList.addAll(results);
                } else {
                    list.add(t);
                }
            }
            ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
            elasticSearchResult.setResultList(resultList);
            elasticSearchResult.setDocList(list);
            elasticSearchResult.setTotalHits(stream.getTotalHits());
            return elasticSearchResult;
        } catch (Exception e) {
            return ElasticSearchResult.error("处理结果流错误：" + e.getMessage());
        } finally {
            stream.close();
        }
    }


}
