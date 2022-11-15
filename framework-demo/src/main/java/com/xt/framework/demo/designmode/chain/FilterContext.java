package com.xt.framework.demo.designmode.chain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 类描述:
 * User: chenggangxu@sohu-inc.com
 * Date: 2016/1/19
 * Time: 13:11
 */
public class FilterContext {

    /**
     * 过程变量容器
     */
    private Map<String, Object> environmentMap = Maps.newConcurrentMap();

    public static FilterContext builder() {
        return new FilterContext();
    }

    public void putEnv(String key, Object value) {
        environmentMap.put(key, value);
    }

    public <T> T getEnv(String key, Class<T> clazz) throws Exception {
        Preconditions.checkNotNull(clazz);
        if (environmentMap.containsKey(key)) {
            Object value = environmentMap.get(key);
            if (clazz.isInstance(value)) {
                return (T) value;
            }
        }
        throw new Exception("校验过程参数类型错误!");
    }
}
