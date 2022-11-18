package com.xt.framework.db.mysql.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author tao.xiong
 * @Description 自定义标准分片策略-精准分库算法
 * 哈希算法：策略简单、数据量均匀、请求量均匀、扩容麻烦
 * 范围算法：策略简单、扩容简单、数据量不均、请求量不匀
 * 索引表法：节点扩容无影响、性能下降一倍
 * 缓存映射法：在索引表法基础上多一次cache查询
 * 基因法：业务标识 基因融入 id(类似表加业务字段区分)
 * 一致性hash环 ：扩容只影响环上前一个节点
 * @Date 2022/11/17 15:12
 */
public class DbPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<Long> preciseShardingValue) {
        //databaseNames 所有分片库的集合,
        // preciseShardingValue 为分片属性，其中 logicTableName 为逻辑表，columnName 分片健（字段），value 为从 SQL 中解析出的分片健的值
        for (String databaseName : databaseNames) {
            //哈希
            String value = preciseShardingValue.getValue() % databaseNames.size() + "";
            if (databaseName.endsWith(value)) {
                return databaseName;
            }
        }
        throw new IllegalArgumentException();
    }
}
