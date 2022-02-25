package com.xt.framwork.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.xt.framwork.common.core.util.JsonUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author tao.xiong
 * @Description 时间格式化
 * @Date 2022/2/18 15:30
 */
@Configuration
public class JsonConfig {

    /**
     * Date格式化字符串 yyyy-MM-dd
     */
    private static final String DATE_FORMAT = DatePattern.NORM_DATE_PATTERN;
    /**
     * DateTime格式化字符串 yyyy-MM-dd HH:mm:ss
     */
    private static final String DATETIME_FORMAT = DatePattern.NORM_DATETIME_PATTERN;
    /**
     * Time格式化字符串 HH:mm:ss
     */
    private static final String TIME_FORMAT = DatePattern.NORM_TIME_PATTERN;

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.configure(new ObjectMapper().registerModule(new JavaTimeModule()
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_FORMAT))
                ))
                .registerModule(new SimpleModule().addSerializer(Long.class, ToStringSerializer.instance)
                        .addSerializer(Long.TYPE, ToStringSerializer.instance)
                        .addSerializer(SerializerProvider.class, new JsonUtils.MyDtoNullKeySerializer()))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.INDENT_OUTPUT)
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature())
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature())
                .enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature())
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature())
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature())
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature())

        );
    }
}

