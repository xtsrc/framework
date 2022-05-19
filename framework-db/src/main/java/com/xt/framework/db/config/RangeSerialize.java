package com.xt.framework.db.config;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/5/19 15:27
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = RangeSerializer.class)
@JsonDeserialize(using = RangeDeserializer.class)
public @interface RangeSerialize {
}
