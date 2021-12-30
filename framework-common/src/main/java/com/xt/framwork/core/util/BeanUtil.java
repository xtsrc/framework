package com.xt.framwork.core.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author tao.xiong
 * @Description bean 操作
 * @Date 2021/9/23 13:41
 */
public class BeanUtil extends BeanUtils {
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Class<T> tClass) {
        if (ObjectUtils.isEmpty(sources)) {
            return Lists.newArrayList();
        } else {
            String oldOb = JSON.toJSONString(sources);
            return JSON.parseArray(oldOb, tClass);
        }
    }


    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, ColaBeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

    @FunctionalInterface
    public interface ColaBeanUtilsCallBack<S, T> {

        /**
         * 自定义处理
         *
         * @param t target
         * @param s source
         */
        void callBack(S s, T t);
    }
}
