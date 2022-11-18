package com.xt.framework.db.mysql.algorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author tao.xiong
 * @Description 自定义复合分片策略
 * @Date 2022/11/17 16:25
 */
public class DbComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {


    @Override
    public Collection<String> doSharding(Collection<String> databaseNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {

        // 得到每个分片健对应的值
        Collection<Long> orderIdValues = this.getShardingValue(complexKeysShardingValue, "id");
        Collection<Long> userIdValues = this.getShardingValue(complexKeysShardingValue, "user_id");

        List<String> shardingSuffix = new ArrayList<>();
        // 对两个分片健同时取模的方式分库
        for (Long userId : userIdValues) {
            for (Long orderId : orderIdValues) {
                String suffix = userId % databaseNames.size() + "_" + orderId % databaseNames.size();
                for (String databaseName : databaseNames) {
                    if (databaseName.endsWith(suffix)) {
                        shardingSuffix.add(databaseName);
                    }
                }
            }
        }
        return shardingSuffix;
    }

    private Collection<Long> getShardingValue(ComplexKeysShardingValue<Long> shardingValues, final String key) {
        Collection<Long> valueSet = new ArrayList<>();
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey(key)) {
            valueSet.addAll(columnNameAndShardingValuesMap.get(key));
        }
        return valueSet;
    }
}
