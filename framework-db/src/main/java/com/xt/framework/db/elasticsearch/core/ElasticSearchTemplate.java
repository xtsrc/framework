package com.xt.framework.db.elasticsearch.core;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tao.xiong
 * @Description es 查询封装
 * @Date 2022/9/2 10:04
 */
@Slf4j
@Service
public class ElasticSearchTemplate<T> {
    Class<T> innerType;
    String indexName;

    public Class<T> getInnerType() {
        return innerType;
    }

    public void setInnerType(Class<T> innerType) {
        this.innerType = innerType;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public static final String AGG_TERM = "AGG_TERM";
    public static final String AGG_CARDINALITY = "AGG_CARDINALITY";
    public static final long SCROLL_TIME_IN_MILLIS = 1000;
    public static final int SCROLL_SIZE = 100;
    @Resource
    private ElasticsearchOperations elasticsearchOperations;
    @Resource
    public ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private EsResultConverter<T> esResultConverter;


    public void saveOrUpdateById(T t) {
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(t).withId(Objects.requireNonNull(elasticsearchOperations.stringIdRepresentation(BeanUtil.getProperty(t, "id")))).build();
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexName));
    }

    public void batchSaveOrUpdateById(List<T> list) {
        List<IndexQuery> queries = list.stream().map(t -> new IndexQueryBuilder().withObject(t).build()).collect(Collectors.toList());
        elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(indexName));
    }

    public void update(T t, QueryBuilder queryBuilder) {
        T updateOne = searchOne(queryBuilder);
        if (updateOne == null) {
            log.error("es按条件查询更新，未找到！：查询条件：{}", queryBuilder);
            return;
        }
        UpdateQuery updateQuery = UpdateQuery.builder(Objects.requireNonNull(elasticsearchOperations.stringIdRepresentation(BeanUtil.getProperty(updateOne, "id"))))
                .withDocument(Document.parse(JSON.toJSONString(t))).build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of(indexName));
    }

    public ElasticSearchResult<T> list(QueryBuilder queryBuilder) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        SearchHits<T> searchHits = elasticsearchOperations.search(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        return esResultConverter.handleBatch(searchHits);
    }

    public ElasticSearchResult<T> page(QueryBuilder queryBuilder, int page, int size) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(PageRequest.of(page, size)).build();
        SearchHits<T> searchHits = elasticsearchOperations.search(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        return esResultConverter.handleBatch(searchHits);
    }

    public T searchOne(QueryBuilder queryBuilder) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        SearchHit<T> searchHit = elasticsearchOperations.searchOne(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        return searchHit == null ? null : searchHit.getContent();
    }

    public ElasticSearchResult<T> agg(QueryBuilder queryBuilder, String groupBy, String distinctBy, Long minValue, Long maxValue) {
        if (StringUtils.isEmpty(groupBy)) {
            return ElasticSearchResult.error("分组参数不能为空！");
        }
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(AGG_TERM).field(groupBy);
        if (!StringUtils.isEmpty(distinctBy)) {
            CardinalityAggregationBuilder cardinalityAggregationBuilder = AggregationBuilders.cardinality(AGG_CARDINALITY).field(distinctBy);
            termsAggregationBuilder.subAggregation(cardinalityAggregationBuilder);
        }
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().addAggregation(termsAggregationBuilder).withQuery(queryBuilder).build();
        //聚合查询主要是统计，不需要具体哪些记录
        nativeSearchQuery.setMaxResults(0);
        Aggregations aggregations = elasticsearchOperations.search(nativeSearchQuery, innerType, IndexCoordinates.of(indexName)).getAggregations();
        return esResultConverter.handleAgg(aggregations, minValue, maxValue);
    }

    public void scan(QueryBuilder queryBuilder, Consumer<T> consumer) {
        NativeSearchQuery nativeSearchQuery = scanQuery(queryBuilder);
        SearchHitsIterator<T> stream = elasticsearchOperations.searchForStream(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        esResultConverter.handleStream(stream, consumer);
    }

    public void scroll(QueryBuilder queryBuilder, Sort sort, Consumer<T> consumer) {
        NativeSearchQuery nativeSearchQuery = scrollQuery(queryBuilder, sort);
        SearchHitsIterator<T> stream = elasticsearchOperations.searchForStream(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        esResultConverter.handleStream(stream, consumer);
    }

    public <R> List<R> scan(QueryBuilder queryBuilder, Function<T, R> function) {
        NativeSearchQuery nativeSearchQuery = scanQuery(queryBuilder);
        SearchHitsIterator<T> stream = elasticsearchOperations.searchForStream(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        return esResultConverter.handleStream(stream, function);
    }

    public <R> List<R> scroll(QueryBuilder queryBuilder, Sort sort, Function<T, R> function) {
        NativeSearchQuery nativeSearchQuery = scrollQuery(queryBuilder, sort);
        SearchHitsIterator<T> stream = elasticsearchOperations.searchForStream(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        return esResultConverter.handleStream(stream, function);
    }

    public ElasticSearchResult<T> scan(QueryBuilder queryBuilder, String scrollId) {
        NativeSearchQuery nativeSearchQuery = scanQuery(queryBuilder);
        return esResultConverter.handleScroll(this, scrollHits(nativeSearchQuery, scrollId));
    }

    public ElasticSearchResult<T> scroll(QueryBuilder queryBuilder, Sort sort, String scrollId) {
        NativeSearchQuery nativeSearchQuery = scrollQuery(queryBuilder, sort);
        return esResultConverter.handleScroll(this, scrollHits(nativeSearchQuery, scrollId));
    }

    protected NativeSearchQuery scrollQuery(QueryBuilder queryBuilder, Sort sort) {
        return new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, SCROLL_SIZE, sort)).withQuery(queryBuilder).build();
    }

    protected NativeSearchQuery scanQuery(QueryBuilder queryBuilder) {
        return new NativeSearchQueryBuilder().withSearchType(SearchType.fromString("SCAN"))
                .withPageable(PageRequest.of(0, SCROLL_SIZE)).withQuery(queryBuilder).build();
    }

    protected SearchScrollHits<T> scrollHits(NativeSearchQuery nativeSearchQuery, String scrollId) {
        SearchScrollHits<T> scroll;
        if (StringUtils.isEmpty(scrollId)) {
            scroll = elasticsearchRestTemplate.searchScrollStart(SCROLL_TIME_IN_MILLIS, nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        } else {
            scroll = elasticsearchRestTemplate.searchScrollContinue(scrollId, SCROLL_TIME_IN_MILLIS, innerType, IndexCoordinates.of(indexName));
        }
        return scroll;
    }


}
