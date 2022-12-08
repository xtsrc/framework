package com.xt.framwork.common.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Date 2022/12/08
 * @author tao.xiong
 * @Description 泛型
 */
public class GenericSuperclassUtil {

    public static Class<?> getActualTypeArgument(Class<?> clazz) {
        Class<?> entityClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entityClass = (Class<?>) actualTypeArguments[0];
            }
        }
        return entityClass;
    }

    public static Class<?> getActualTypeArgument(Class<?> clazz,int i) {
        Class<?> entityClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entityClass = (Class<?>) actualTypeArguments[i];
            }
        }
        return entityClass;
    }


}
