package com.xt.framework.db.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author tao.xiong
 * @Description 前端反序列化
 * @Date 2022/5/19 14:52
 */
public class RangeDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String range = p.getText().trim();
        if (range.isEmpty()) {
            return null;
        }
        try {
            return (T) LocalDate.parse(range, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return (T) range;
        }
    }



}
