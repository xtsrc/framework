package com.xt.framework.db.elasticsearch.template;

import com.xt.framework.db.elasticsearch.core.ElasticSearchTemplate;
import com.xt.framework.db.elasticsearch.template.model.Order;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.ParsedSimpleValue;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderElasticSearchTemplate extends ElasticSearchTemplate<Order> {
    public OrderElasticSearchTemplate() {
        this.setInnerType(Order.class);
        this.setIndexName("order");
    }

    public void termsAgg() {
        String aggName = "status_bucket";
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(PageRequest.of(0, 1));
        TermsAggregationBuilder termsAgg = AggregationBuilders.terms(aggName).field("status").size(0);
        queryBuilder.addAggregation(termsAgg);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        assert aggregations != null;
        Terms terms = aggregations.get(aggName);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        HashMap<String, Long> statusRes = new HashMap<>();
        buckets.forEach(bucket -> {
            statusRes.put(bucket.getKeyAsString(), bucket.getDocCount());
        });
        System.out.println("---聚合结果---");
        System.out.println(statusRes);
    }

    public void dateHistogramAgg() {
        String aggName = "date";
        DateHistogramAggregationBuilder dateHistogramAggregation = AggregationBuilders.dateHistogram(aggName).field("create_time")
                .calendarInterval(DateHistogramInterval.days(1)).format("yyyy-MM-dd");
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(PageRequest.of(0, 1)).addAggregation(dateHistogramAggregation);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        assert aggregations != null;
        ParsedDateHistogram terms = aggregations.get(aggName);
        List<? extends Histogram.Bucket> buckets = terms.getBuckets();
        HashMap<String, Long> resultMap = new HashMap<>();
        buckets.forEach(bucket -> {
            resultMap.put(bucket.getKeyAsString(), bucket.getDocCount());
        });
        System.out.println("---聚合结果---");
        System.out.println(resultMap);
    }

    public void rangeAgg() {
        String aggName = "range";
        RangeAggregationBuilder agg = AggregationBuilders.range(aggName).field("amount").addUnboundedTo(100).addRange(100, 200).addUnboundedFrom(200);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(PageRequest.of(0, 1)).addAggregation(agg);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        assert aggregations != null;
        ParsedRange terms = aggregations.get(aggName);
        List<? extends Range.Bucket> buckets = terms.getBuckets();
        HashMap<String, Long> resultMap = new HashMap<>();
        buckets.forEach(bucket -> {
            resultMap.put(bucket.getKeyAsString(), bucket.getDocCount());
        });
        System.out.println("---聚合结果---");
        System.out.println(resultMap);
    }

    public void nestedAgg() {
        String aggName = "product_nested";
        String termsAggName = "name_bucket";
        NestedAggregationBuilder aggregationBuilder = AggregationBuilders.nested(aggName, "product").subAggregation(AggregationBuilders.terms(termsAggName).field("product.name"));
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .addAggregation(aggregationBuilder);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        assert aggregations != null;
        ParsedNested nestedRes = aggregations.get(aggName);
        Terms terms = nestedRes.getAggregations().get(termsAggName);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        HashMap<String, Long> resMap = new HashMap<>();
        buckets.forEach(bucket -> {
            resMap.put(bucket.getKeyAsString(), bucket.getDocCount());
        });
        System.out.println("---聚合结果---");
        System.out.println(resMap);
    }

    public void sumAgg() {
        String aggName = "sumAmount";
        SumAggregationBuilder agg = AggregationBuilders.sum(aggName).field("amount");
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .withQuery(QueryBuilders.rangeQuery("create_time").format("yyyy-MM-dd").from("2022-05-01").to("2022-05-01"))
                .addAggregation(agg);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        assert aggregations != null;
        ParsedSum metric = aggregations.get(aggName);
        double value = metric.getValue();
        System.out.println("---聚合结果---");
        System.out.println(value);
    }

    public void scriptAgg() {
        String totalAmountAggName = "total_amount";
        String totalQuantityAggName = "total_quantity";
        SumAggregationBuilder amountAgg = AggregationBuilders.sum(totalAmountAggName).field("amount");
        SumAggregationBuilder quantityAgg = AggregationBuilders.sum(totalQuantityAggName).script(
                new Script("int total = 0;\n" +
                        "            for(int i=0; i<params._source['product'].size(); i++){\n" +
                        "              if(params._source['product'][i]['quantity'] != null){\n" +
                        "                total += params._source['product'][i]['quantity'];\n" +
                        "              }\n" +
                        "            }\n" +
                        "            return total;"));

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .addAggregation(amountAgg).addAggregation(quantityAgg);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        assert aggregations != null;
        ParsedSum amountRes = aggregations.get(totalAmountAggName);
        ParsedSum quantityRes = aggregations.get(totalQuantityAggName);
        double avgPrice = amountRes.getValue() / quantityRes.getValue();
        System.out.println("---聚合结果---");
        System.out.println(avgPrice);
    }

    public void bucketSortAgg() {
        String aggName = "order_bucket";
        String totalAmountAggName = "total_amount";
        String totalQuantityAggName = "total_quantity";
        String avgPriceAggName = "avg_price";
        String bucketSortAggName = "avg_price_sort";
        HashMap<String, String> bucketsPath = new HashMap<>();
        bucketsPath.put("amount", "total_amount");
        bucketsPath.put("quantity", "total_quantity");
        List<FieldSortBuilder> sortList = new ArrayList<>();
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("avg_price").order(SortOrder.DESC);
        sortList.add(fieldSortBuilder);
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(aggName).field("no")
                .subAggregation(AggregationBuilders.sum(totalAmountAggName).field("amount"))
                .subAggregation(AggregationBuilders.sum(totalQuantityAggName).script(
                        new Script("int total = 0;\n" +
                                "            for(int i=0; i<params._source['product'].size(); i++){\n" +
                                "              if(params._source['product'][i]['quantity'] != null){\n" +
                                "                total += params._source['product'][i]['quantity'];\n" +
                                "              }\n" +
                                "            }\n" +
                                "            return total;")
                ))
                .subAggregation(PipelineAggregatorBuilders.bucketScript(avgPriceAggName, bucketsPath,
                        new Script("params.amount / params.quantity")))
                .subAggregation(PipelineAggregatorBuilders.bucketSort(bucketSortAggName, sortList).from(0).size(5));
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, 1))
                .addAggregation(aggregationBuilder);
        Aggregations aggregations = elasticsearchRestTemplate.search(queryBuilder.build(), Order.class).getAggregations();
        // 因为要求按序输出，所以这里使用LinkedHashMap，HashMap不会按照顺序显示
        LinkedHashMap<String, Double> resultMap = new LinkedHashMap<>();
        assert aggregations != null;
        Terms terms = aggregations.get(aggName);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> {
            ParsedSimpleValue avgRes = bucket.getAggregations().get(avgPriceAggName);

            resultMap.put(bucket.getKeyAsString(), Double.parseDouble(avgRes.getValueAsString()));

        });
        System.out.println("---聚合结果---");
        System.out.println(resultMap);
    }

}
