package com.xt.framwork.core.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tao.xiong
 */
public final class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature());
        MAPPER.enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature());
        MAPPER.enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature());
        MAPPER.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        MAPPER.enable(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature());
        MAPPER.enable(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature());
        MAPPER.enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature());
        MAPPER.getSerializerProvider().setNullKeySerializer(new MyDtoNullKeySerializer());
        // 序列化不需要null的
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static String encode(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (Exception e) {
            logger.error("serialize data error:{}", data, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> decodeMap(String json, TypeReference<Map<K, V>> ref) {
        try {
            return MAPPER.readValue(json, ref);
        } catch (Exception e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    public static Map<String, Object> decodeMap(ObjectNode json) {
        try {
            return MAPPER.convertValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    /**
     * 反向序列化
     *
     * @param content       值
     * @param typeReference 类
     * @param <T>           对象
     * @return 反序列化对象
     */
    public static <T> T parseJson(String content, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(content, typeReference);
        } catch (IOException e) {
            logger.error("deserialize TypeReference error: {}", content, e);
            throw new RuntimeException("parseJson error", e);
        }
    }

    public static <T> T parseJson(String content, Class<T> toValueType) {
        try {
            if (StringUtils.isEmpty(content)) {
                return null;
            }
            return MAPPER.readValue(content, toValueType);
        } catch (Exception e) {
            logger.error("de serialize object error: {} {}", encode(content), encode(toValueType), e);
            return null;
        }
    }

    public static <T> T parseJson(ObjectNode content, Class<T> toValueType) {
        try {
            if (content == null) {
                return null;
            }
            String obj = MAPPER.writeValueAsString(content);
            return MAPPER.readValue(obj, toValueType);
        } catch (Exception e) {
            logger.error("de serialize object error: {} {}", encode(content), encode(toValueType), e);
            return null;
        }
    }

    public static <T> T readUpdate(ObjectNode node, T updateValue) {
        if (node == null) {
            return null;
        }
        ObjectReader objectReader = MAPPER.readerForUpdating(updateValue);
        try {
            return objectReader.readValue(node);
        } catch (IOException e) {
            logger.error("readUpdate object error: {} {}", encode(node), encode(updateValue), e);
            return null;
        }
    }

    public static ObjectNode transToNode(String content) throws IOException {
        return (ObjectNode) MAPPER.readTree(content);
    }

    public static String getAsString(String content, String path) throws IOException {
        JsonNode root = MAPPER.readTree(content);
        JsonNode targetNode = root.get(path);
        if (targetNode != null) {
            return targetNode.asText();
        }
        return null;
    }

    public static double atAsDouble(String content, String path) throws IOException {
        JsonNode root = MAPPER.readTree(content);
        JsonNode targetNode = root.at(path);
        if (targetNode != null) {
            return targetNode.asDouble();
        }
        return 0L;
    }

    public static String atAsString(String content, String path) throws IOException {
        JsonNode root = MAPPER.readTree(content);
        JsonNode targetNode = root.at(path);
        if (targetNode != null) {
            return targetNode.asText();
        }
        return "";
    }

    public static Long getAsLong(String content, String path) throws IOException {
        JsonNode root = MAPPER.readTree(content);
        JsonNode targetNode = root.at(path);
        if (targetNode != null) {
            return targetNode.asLong();
        }
        return 0L;
    }

    public static JsonNode getJson(Object object) {
        try {
            String json = MAPPER.writeValueAsString(object);
            return MAPPER.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException("序列化异常");
        }
    }

    public static <T> List<T> getAsList(String json, Class<T> elementClass) throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, elementClass);
        return MAPPER.readValue(json, javaType);
    }

    public static int getListCount(String content, String path) throws IOException {
        JsonNode root = MAPPER.readTree(content);
        JsonNode targetNode = root.at(path);
        if (targetNode == null) {
            return 0;
        }
        return targetNode.size();
    }

    public static <T> List<T> getAsList(String json, String path, Class<T> elementClass) throws IOException {
        JsonNode root = MAPPER.readTree(json);
        JsonNode targetNode = root.at(path);
        if (targetNode == null) {
            return Lists.newArrayList();
        }
        if (targetNode.isNull()) {
            return Lists.newArrayList();
        }
        String targetString = targetNode.toString();
        if (StringUtils.isEmpty(targetString)) {
            return Lists.newArrayList();
        }
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, elementClass);
        return MAPPER.readValue(targetString, javaType);
    }

    public static <T> List<T> copy(List<?> list, Class<T> clazz) {
        if (ObjectUtils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            String oldOb = JSON.toJSONString(list);
            return JSON.parseArray(oldOb, clazz);
        }
    }

    private static class MyDtoNullKeySerializer extends StdSerializer<Object> {

        private static final long serialVersionUID = 3024090892228000517L;

        MyDtoNullKeySerializer() {
            this(null);
        }

        MyDtoNullKeySerializer(Class<Object> t) {
            super(t);
        }

        @Override
        public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
                throws IOException {
            jsonGenerator.writeFieldName("");
        }

    }

}
