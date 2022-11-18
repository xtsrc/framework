package com.xt.framework.db.mysql.algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author tao.xiong
 * @Description 自定义标准分片策略-范围分表算法
 * @Date 2022/11/17 15:57
 */
public class TableRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<Long> rangeShardingValue) {

        Set<String> result = new LinkedHashSet<>();
        // between and 的起始值
        long lower = rangeShardingValue.getValueRange().lowerEndpoint();
        long upper = rangeShardingValue.getValueRange().upperEndpoint();
        // 循环范围计算分表逻辑
        for (long i = lower; i <= upper; i++) {
            for (String tableName : tableNames) {
                if (tableName.endsWith(i % tableNames.size() + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }
}
