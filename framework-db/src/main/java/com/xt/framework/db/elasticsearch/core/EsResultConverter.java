package com.xt.framework.db.elasticsearch.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate.*;

/**
 * @author tao.xiong
 * @Description Es 结果处理类
 * @Date 2022/9/19 15:00
 */
@Service
@Slf4j
public class EsResultConverter<T>{
    private EsResultConverter() {
        throw new IllegalStateException("Utility class");
    }

    protected void handleStream(SearchHitsIterator<T> stream, Consumer<T> consumer) {
        while (stream.hasNext()) {
            consumer.accept(stream.next().getContent());
        }
    }

    protected <R> List<R> handleStream(SearchHitsIterator<T> stream, Function<T, R> function) {
        List<R> list = new ArrayList<>();
        while (stream.hasNext()) {
            list.add(function.apply(stream.next().getContent()));
        }
        return list;
    }
    protected ElasticSearchResult<T> handleBatch(SearchHits<T> searchHits) {
        ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
        elasticSearchResult.setTotalHits(searchHits.getTotalHits());
        elasticSearchResult.setDocList(searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList()));
        return elasticSearchResult;
    }

    protected ElasticSearchResult<T> handleScroll(ElasticSearchTemplate<T> elasticSearchTemplate,SearchScrollHits<T> scroll) {
        String scrollId = scroll.getScrollId();
        List<T> list = scroll.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        while (scroll.hasSearchHits()) {
            scrollId = scroll.getScrollId();
            scroll = elasticSearchTemplate.elasticsearchRestTemplate.searchScrollContinue(scrollId, SCROLL_TIME_IN_MILLIS,
                    elasticSearchTemplate.getInnerType(), IndexCoordinates.of(elasticSearchTemplate.getIndexName()));
            list.addAll(scroll.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()));
        }
        elasticSearchTemplate.elasticsearchRestTemplate.searchScrollClear(Collections.singletonList(scrollId));
        ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
        elasticSearchResult.setScrollId(scrollId);
        elasticSearchResult.setDocList(list);
        return elasticSearchResult;
    }

    protected ElasticSearchResult<T> handleAgg(Aggregations aggregations, Long minValue, Long maxValue) {
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
            if (minValue != null && count < minValue) {
                return;
            }
            if (maxValue != null && count > maxValue) {
                return;
            }
            resultMap.put(buck.getKeyAsString(), count);
        });
        ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
        elasticSearchResult.setAggCountMap(resultMap);
        return elasticSearchResult;
    }

}
