package com.xt.framework.common.core.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

/**
 * @author tao.xiong
 * @Description TODO
 * @Date 2021/8/11 10:07
 */
public class JsonArrayHandler2 extends FastjsonTypeHandler {

    private final Class<? extends Object> type;

    public JsonArrayHandler2(Class<?> type) {
        super((Class<Object>) type);
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        return JSON.parseArray(json, this.type);
    }

    @Override
    protected String toJson(Object obj) {
        return super.toJson(obj);
    }
}
