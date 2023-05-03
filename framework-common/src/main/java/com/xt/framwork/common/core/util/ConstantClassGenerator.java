package com.xt.framwork.common.core.util;

import com.xt.framwork.common.core.bean.BatchRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Function;

/**
 * @author tao.xiong
 * @date 2023/4/13 18:35
 * @desc 常量类生成工具
 */
public class ConstantClassGenerator {
    public static void generateConstants(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            String constantName = ObjectUtil.camelCaseToUpperCaseUnderscore(fieldName);
            System.out.println("public static final String " + constantName + " = \"" + fieldName + "\";");
        }
    }
    public static String getFieldName(Function<?,?> function){
        return "";
    }

    public static void main(String[] args) {
        generateConstants(BatchRequest.class);
    }
}
