package com.xt.framework.db.elasticsearch.core;

import lombok.Data;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author tao.xiong
 * @Description es 请求
 * @Date 2022/10/24 17:02
 */
@Data
public class ElasticSearchRequest<T, R> {
    /**
     * 常量
     */
    public static final String AGG_TERM = "AGG_TERM";
    public static final String AGG_CARDINALITY = "AGG_CARDINALITY";
    public static final long SCROLL_TIME_IN_MILLIS = 1000;
    public static final int BATCH_SIZE = 500;
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
    private ProcessReq<T, R> processReq;
    /**
     * 批量请求
     */
    private BatchReq batchReq;

    @Data
    public static class AggReq {
        private String groupBy;
        private String distinctBy;
        private Long minCount;
        private Long maxCount;
    }

    @Data
    public static class ProcessReq<T, R> {
        private Consumer<T> consumer;
        private Function<T, R> function;
    }

    @Data
    public static class BatchReq {
        private Sort sort;
        private Integer page=0;
        private Integer size=500;
        private SearchType searchType=SearchType.DEFAULT;
        private String scrollId;
    }

    protected NativeSearchQuery buildQuery() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
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
            nativeSearchQuery.setPageable(PageRequest.of(batchReq.getPage(), batchReq.getSize()));
            nativeSearchQuery.setSearchType(batchReq.getSearchType());
            if(batchReq.getSort()!=null){
                nativeSearchQuery.addSort(batchReq.getSort());
            }
        }
        return nativeSearchQuery;
    }
}
