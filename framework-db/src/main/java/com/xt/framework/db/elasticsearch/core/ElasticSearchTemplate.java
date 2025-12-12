package com.xt.framework.db.elasticsearch.core;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xt.framework.db.elasticsearch.config.ElasticSearchClientConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
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
import java.io.IOException;
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
    @Setter
    Class<T> innerType;
    @Setter
    String indexName;
    /**
     * 直接实现ElasticsearchOperations 提供更细粒度的控制，适合复杂查询或非标准操作
     */
    @Resource
    public ElasticsearchRestTemplate elasticsearchRestTemplate;

    public static BulkProcessor buildProcessor() {
        try (RestHighLevelClient tempClient = ElasticSearchClientConfig.buildClient()) {
            return BulkProcessor.builder((request, bulkListener) -> tempClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener)
                            , new BulkProcessor.Listener() {
                                @Override
                                public void beforeBulk(long l, BulkRequest bulkRequest) {
                                    log.info("操作{}条数据", bulkRequest.numberOfActions());
                                }

                                @Override
                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    log.info("成功：{}条数据，用时：{}", bulkRequest.numberOfActions(), bulkResponse.getTook());
                                }

                                @Override
                                public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                                    log.info("失败：{}条数据", bulkRequest.numberOfActions(), throwable);
                                }
                            })
                    .setBulkActions(5000)//5000条flush一次
                    .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))//10MB flush一次
                    .setFlushInterval(TimeValue.timeValueSeconds(5))//5秒flush一次
                    .setConcurrentRequests(4)//并发数
                    //EsRejectedExecutionException造成N个bulk内request失败重试，初始等待100ms,后面指数增加，共重试三次
                    .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                    .build();
        } catch (IOException e) {
            log.error("buildBulkProcessor error", e);
            return null;
        }
    }


    public void saveOrUpdateById(T t) {
        Assert.notNull(t, "save or update param must not be null");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(t).withId(Objects.requireNonNull(elasticsearchRestTemplate
                .stringIdRepresentation(BeanUtil.getProperty(t, "id")))).build();
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of(indexName));
    }

    /**
     * 封装底层bulkRequest,依赖配置调优
     *
     * @param list source
     */
    public void batchSaveOrUpdateById(List<T> list) {
        Assert.notEmpty(list, "batch save or update param must not be empty");
        List<IndexQuery> queries = list.stream().map(t -> new IndexQueryBuilder().withObject(t)
                .withId(Objects.requireNonNull(elasticsearchRestTemplate.stringIdRepresentation(BeanUtil.getProperty(t, "id"))))
                .build()).collect(Collectors.toList());
        elasticsearchRestTemplate.bulkIndex(queries, IndexCoordinates.of(indexName));
    }

    /**
     * 控制粒度：提供细粒度控制，适合需要精确管理执行时机的场景（如将批量操作嵌入复杂事务流程）
     * 异步与自动化管理：需用户自行处理异步回调或同步等待结果。
     * 资源与性能‌：无此类开销，但需避免手动执行频率过高引发网络压力
     * 错误处理‌：支持通过监听器捕获执行结果或失败事件，需在每次bulk()调用后单独解析BulkResponse。‌
     * 选型：低延迟控制或操作间存在依赖关系
     *
     * @param list source
     */
    public void bulkSaveOrUpdateById(List<T> list) {
        try (RestHighLevelClient tempClient = ElasticSearchClientConfig.buildClient()) {
            Assert.notEmpty(list, "batch save or update param must not be empty");
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout(TimeValue.timeValueMinutes(2));
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            list.forEach(t -> bulkRequest.add(new IndexRequest(indexName)
                    .id(Objects.requireNonNull(elasticsearchRestTemplate.stringIdRepresentation(BeanUtil.getProperty(t, "id"))))
                    .source(t)));
            BulkResponse bulkResponse = tempClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                //单个失败不会影响其他操作，需要解析
                log.error(bulkResponse.buildFailureMessage());
            }
        } catch (Exception e) {
            log.error("bulkSaveOrUpdateById error", e);
        }

    }

    /**
     * 控制粒度：自动化、适合高吞吐量、持续性的批量数据处理（如日志摄入和数据同步）
     * 异步与自动化管理：内置异步执行和指数退避重试机制（通过BackoffPolicy配置），减少了手动干预需求
     * 资源与性能‌：通过后台调度器（如ScheduledThreadPoolExecutor）实现周期性刷新，但可能因锁竞争导致性能问题（如定时任务抢占锁阻塞业务线程）
     * 错误处理‌：支持通过监听器捕获执行结果或失败事件，监听器回调（如afterBulk）能统一处理批量结果
     * 选型：对于大规模、持续的批量写入
     *
     * @param list source
     */
    public void bulkProcessSaveOrUpdateById(List<T> list) {
        try (BulkProcessor bulkProcessor = buildProcessor()) {
            assert bulkProcessor != null;
            Assert.notEmpty(list, "batch save or update param must not be empty");
            list.forEach(t -> bulkProcessor.add(new IndexRequest(indexName)
                    .id(Objects.requireNonNull(elasticsearchRestTemplate.stringIdRepresentation(BeanUtil.getProperty(t, "id"))))
                    .source(t)));
            bulkProcessor.flush();
        } catch (Exception e) {
            log.error("bulkProcess error", e);
        }
    }

    public void deleteById(List<String> ids) {
        Assert.notEmpty(ids, "batch delete param must not be empty");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIds(ids).build();
        elasticsearchRestTemplate.delete(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
    }

    public void update(T t, QueryBuilder queryBuilder) {
        Assert.noNullElements(Lists.newArrayList(t, queryBuilder), "update param must not be null");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        SearchHit<T> searchHit = elasticsearchRestTemplate.searchOne(nativeSearchQuery, innerType, IndexCoordinates.of(indexName));
        if (searchHit == null) {
            log.error("es按条件查询更新，未找到！：查询条件：{}", queryBuilder);
            return;
        }
        T updateOne = searchHit.getContent();
        UpdateQuery updateQuery = UpdateQuery.builder(Objects.requireNonNull(elasticsearchRestTemplate.stringIdRepresentation(BeanUtil.getProperty(updateOne, "id"))))
                .withDocument(Document.parse(JSON.toJSONString(t))).build();
        elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of(indexName));
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
        return ElasticSearchResult.getResult(elasticSearchRequest, elasticsearchRestTemplate.search(nativeSearchQuery, innerType, IndexCoordinates.of(indexName)));
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
        return ElasticSearchResult.getResult(elasticSearchRequest, elasticsearchRestTemplate.searchForStream(nativeSearchQuery, innerType, IndexCoordinates.of(indexName)));
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
            scroll = getScrollHits(nativeSearchQuery, scrollTimeInMills, scrollId);
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
