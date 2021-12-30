package com.xt.framwork.core.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author tao.xiong
 * @Description json array handler
 * @Date 2021/8/4 14:55
 */
public class JsonArrayHandler<T> extends AbstractJsonTypeHandler<T> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JavaType type;

    public JsonArrayHandler(Class<?> innerType) {
        this.type = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, innerType);
    }

    @Override
    public T parse(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
