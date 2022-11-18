package com.xt.framework.db.mysql.algorithm;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tao.xiong
 * @Description hit分库算法
 * @Date 2022/11/17 16:38
 */
public class DbHintShardingAlgorithm implements HintShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> databaseNames, HintShardingValue<String> hintShardingValue) {

        Collection<String> result = new ArrayList<>();
        for (String databaseName : databaseNames) {
            for (String shardingValue : hintShardingValue.getValues()) {
                if (databaseName.endsWith(String.valueOf(Long.parseLong(shardingValue) % databaseNames.size()))) {
                    result.add(databaseName);
                }
            }
        }
        return result;
    }
}
