package com.xt.framework.common.core.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

/**
 * @author tao.xiong
 * @Description TODO
 * @Date 2021/8/11 10:07
 */
public class JsonArrayHandler2<T> extends FastjsonTypeHandler {

    private final Class<T> type;

    public JsonArrayHandler2(Class<T> type) {
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
    public static class ListStringHandler extends JsonArrayHandler2<String> {
        public ListStringHandler() {
            super(String.class);
        }
    }
}