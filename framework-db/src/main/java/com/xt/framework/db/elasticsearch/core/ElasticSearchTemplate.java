package com.xt.framework.db.elasticsearch.core;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tao.xiong
 * @Description es 查询封装
 * @Date 2022/9/2 10:04
 */
@Slf4j
@Service
public class ElasticSearchTemplate<T> {
    /**
     * 过程变量
     */
    public static final String AGG_TERM = "AGG_TERM";
    public static final String AGG_CARDINALITY = "AGG_CARDINALITY";
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

    @Resource
    private ElasticsearchOperations elasticsearchOperations;
    @Resource
    public ElasticsearchRestTemplate elasticsearchRestTemplate;


    public void saveOrUpdateById(T t) {
        Assert.notNull(t, "save or update param must not be null");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(t).withId(Objects.requireNonNull(elasticsearchOperations
                .stringIdRepresentation(BeanUtil.getProperty(t, "id")))).build();
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexName));
    }

    public void batchSaveOrUpdateById(List<T> list) {
        Assert.notEmpty(list, "batch save or update param must not be empty");
        List<IndexQuery> queries = list.stream().map(t -> new IndexQueryBuilder().withObject(t)
                .withId(Objects.requireNonNull(elasticsearchOperations.stringIdRepresentation(BeanUtil.getProperty(t, "id"))))
                .build()).collect(Collectors.toList());
        elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(indexName));
    }

    public void deleteById(List<String> ids) {
        Assert.notEmpty(ids, "batch delete param must not be empty");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIds(ids).build();
        elasticsearchOperations.delete(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
    }

    public void update(T t, QueryBuilder queryBuilder) {
        Assert.noNullElements(Lists.newArrayList(t, queryBuilder), "update param must not be null");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        SearchHit<T> searchHit = elasticsearchOperations.searchOne(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        if (searchHit == null) {
            log.error("es按条件查询更新，未找到！：查询条件：{}", queryBuilder);
            return;
        }
        T updateOne = searchHit.getContent();
        UpdateQuery updateQuery = UpdateQuery.builder(Objects.requireNonNull(elasticsearchOperations.stringIdRepresentation(BeanUtil.getProperty(updateOne, "id"))))
                .withDocument(Document.parse(JSON.toJSONString(t))).build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of(indexName));
    }

    /**
     * 通用查询
     *
     * @param elasticSearchRequest 请求
     * @return 结果
     */
    public ElasticSearchResult<T> query(ElasticSearchRequest<T> elasticSearchRequest) {
        Assert.notNull(elasticSearchRequest, "query param must not be null");
        NativeSearchQuery nativeSearchQuery = elasticSearchRequest.buildQuery();
        log.info("查询es,index:{},dsl:{}", indexName, nativeSearchQuery.getQuery());
        if (elasticSearchRequest.getBatchReq() != null && elasticSearchRequest.getBatchReq().getPage() == 0) {
            String scrollId = elasticSearchRequest.getBatchReq().getScrollId();
            TimeValue timeValue = elasticSearchRequest.getBatchReq().getTimeValue();
            SearchScrollHits<T> scroll = getScrollHits(nativeSearchQuery, timeValue.getMillis(), scrollId);
            return ElasticSearchResult.getResult(elasticSearchRequest, scroll);
        }
        return ElasticSearchResult.getResult(elasticSearchRequest, elasticsearchOperations.search(nativeSearchQuery, innerType, IndexCoordinates.of(indexName)));
    }

    /**
     * 一次性大批量数据流处理
     *
     * @param elasticSearchRequest 请求
     * @return 结果
     */
    public ElasticSearchResult<T> dealWithStream(ElasticSearchRequest<T> elasticSearchRequest) {
        Assert.notNull(elasticSearchRequest, "deal with  stream param must not be null");
        NativeSearchQuery nativeSearchQuery = elasticSearchRequest.buildQuery();
        log.info("流式处理es,index:{},dsl:{}", indexName, nativeSearchQuery.getQuery());
        return ElasticSearchResult.getResult(elasticSearchRequest, elasticsearchOperations.searchForStream(nativeSearchQuery, innerType, IndexCoordinates.of(indexName)));
    }

    /**
     * 分段数据流处理
     *
     * @param elasticSearchRequest 请求
     * @return 结果
     */
    public ElasticSearchResult<T> dealWithScroll(ElasticSearchRequest<T> elasticSearchRequest) {
        Assert.notNull(elasticSearchRequest, "deal with  scroll param must not be null");
        Assert.notNull(elasticSearchRequest.getBatchReq(), "deal with  scroll param must not be null");
        Assert.isTrue(elasticSearchRequest.getBatchReq().getPage() == 0, "deal with  scroll page must be 0");
        NativeSearchQuery nativeSearchQuery = elasticSearchRequest.buildQuery();
        log.info("scroll处理es,index:{},dsl:{}", indexName, nativeSearchQuery.getQuery());
        TimeValue timeValue = elasticSearchRequest.getBatchReq().getTimeValue();
        long scrollTimeInMills = timeValue.getMillis();
        String scrollId = elasticSearchRequest.getBatchReq().getScrollId();
        SearchScrollHits<T> scroll = getScrollHits(nativeSearchQuery, scrollTimeInMills, scrollId);
        List<T> list = Lists.newArrayList();
        List<Object> resultList = Lists.newArrayList();
        List<String> clearScrollIds = Lists.newArrayList();
        while (scroll.hasSearchHits()) {
            List<T> slice = scroll.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
            List<Object> results = ElasticSearchResult.process(elasticSearchRequest, slice);
            if (!CollectionUtils.isEmpty(results)) {
                resultList.addAll(results);
            } else {
                list.addAll(slice);
            }
            clearScrollIds.add(scrollId);
            scrollId = scroll.getScrollId();
            scroll = elasticsearchRestTemplate.searchScrollContinue(scrollId, scrollTimeInMills,
                    innerType, IndexCoordinates.of(indexName));
        }
        elasticsearchRestTemplate.searchScrollClear(clearScrollIds);
        ElasticSearchResult<T> elasticSearchResult = ElasticSearchResult.success();
        elasticSearchResult.setScrollId(scrollId);
        elasticSearchResult.setDocList(list);
        elasticSearchResult.setResultList(resultList);
        return elasticSearchResult;
    }

    protected SearchScrollHits<T> getScrollHits(NativeSearchQuery nativeSearchQuery, long scrollTimeInMills, String scrollId) {
        SearchScrollHits<T> scroll;
        if (StringUtils.isEmpty(scrollId)) {
            scroll = elasticsearchRestTemplate.searchScrollStart(scrollTimeInMills, nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        } else {
            scroll = elasticsearchRestTemplate.searchScrollContinue(scrollId, scrollTimeInMills, innerType, IndexCoordinates.of(indexName));
        }
        return scroll;
    }
}
