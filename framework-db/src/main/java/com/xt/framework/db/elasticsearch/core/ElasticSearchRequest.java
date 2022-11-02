package com.xt.framework.db.elasticsearch.core;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate.AGG_CARDINALITY;
import static com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate.AGG_TERM;

/**
 * @author tao.xiong
 * @Description es 请求
 * @Date 2022/10/24 17:02
 */
@Data
@Builder
@Slf4j
public class ElasticSearchRequest<T> {
    /**
     * 常量
     */
    public static final long SCROLL_TIME_IN_MILLIS = 60000;
    public static final int BATCH_SIZE = 1000;
    /**
     * 参数
     */
    private QueryBuilder queryBuilder;
    /**
     * 聚合
     */
    private AggReq aggReq;
    /**
     * 结果处理
     */
    private ProcessReq<T> processReq;
    /**
     * 批量请求
     */
    private BatchReq batchReq;

    @Data
    @Builder
    public static class AggReq {
        private String groupBy;
        private String distinctBy;
        private Long minCount;
        private Long maxCount;
    }

    @Data
    @Builder
    public static class ProcessReq<T> {
        private Consumer<List<T>> consumer;
        private Function<List<T>, Object> function;
    }

    @Data
    @Builder
    public static class BatchReq {
        private Sort sort;
        private Integer page;
        private Integer size;
        private SearchType searchType;
        private String scrollId;
        private TimeValue timeValue;
    }

    protected NativeSearchQuery buildQuery() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withSearchType(SearchType.DEFAULT).withQuery(queryBuilder).build();
        if (aggReq != null) {
            assert aggReq.getGroupBy() != null;
            TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(AGG_TERM).field(aggReq.getGroupBy());
            if (!StringUtils.isEmpty(aggReq.getDistinctBy())) {
                CardinalityAggregationBuilder cardinalityAggregationBuilder = AggregationBuilders.cardinality(AGG_CARDINALITY).field(aggReq.getDistinctBy());
                termsAggregationBuilder.subAggregation(cardinalityAggregationBuilder);
            }
            nativeSearchQuery.addAggregation(termsAggregationBuilder);
            //聚合查询主要是统计，不需要具体哪些记录
            nativeSearchQuery.setMaxResults(0);
        }
        if (batchReq != null) {
            if (batchReq.getPage() == null) {
                batchReq.setPage(0);
            }
            if (batchReq.getSize() == null) {
                batchReq.setSize(BATCH_SIZE);
            }
            Assert.isTrue(batchReq.getPage()* batchReq.getSize()<=10000,"分页查询超过最大限制,建议切换为scroll查询");
            if (batchReq.getTimeValue() == null) {
                batchReq.setTimeValue(new TimeValue(SCROLL_TIME_IN_MILLIS));
            }
            nativeSearchQuery.setPageable(PageRequest.of(batchReq.getPage(), batchReq.getSize()));
            nativeSearchQuery.setSearchType(batchReq.getSearchType());
            if (batchReq.getSort() != null) {
                nativeSearchQuery.addSort(batchReq.getSort());
            }
        }
        return nativeSearchQuery;
    }
}
