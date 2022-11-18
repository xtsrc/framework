package com.xt.framework.db.mysql.algorithm;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tao.xiong
 * @Description hit分表算法
 * @Date 2022/11/17 16:38
 */
public class TableHintShardingAlgorithm implements HintShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, HintShardingValue<String> hintShardingValue) {

        Collection<String> result = new ArrayList<>();
        for (String tableName : tableNames) {
            for (String shardingValue : hintShardingValue.getValues()) {
                if (tableName.endsWith(String.valueOf(Long.parseLong(shardingValue) % tableNames.size()))) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }
}
