package com.xt.framwork.common.core.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tao.xiong
 */
@Service
public final class ObjectUtil {
    public static boolean isAllBlank(String... args) {
        return Stream.of(args).allMatch(StringUtils::isNotBlank);
    }

    public static String fetchFirstNotBlank(String... values) {
        return Stream.of(values).filter(StringUtils::isNotBlank).findAny().orElse(null);
    }

    public static Set<String> collectNotBlankToSet(String... values) {
        return Stream.of(values).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    public static boolean isAnyBlank(String... args) {
        return Stream.of(args).anyMatch(StringUtils::isBlank);
    }

    public static boolean isAnyNull(Object... args) {
        return Stream.of(args).anyMatch(Objects::isNull);
    }

    public static Map<String, String> obj2Map(Object obj) throws IllegalAccessException {

        Map<String, String> map = Maps.newHashMap();
        // 获取对象对应类中的所有属性域
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            // 获取原来的访问控制权限
            boolean accessFlag = field.isAccessible();
            // 修改访问控制权限
            field.setAccessible(true);
            // 获取在对象f中属性fields[i]对应的对象中的变量
            Object o = field.get(obj);
            if (o != null) {
                map.put(varName, o.toString());
            }
            // 恢复访问控制权限
            field.setAccessible(accessFlag);

        }
        return map;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>(16);
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static Boolean isSameInGavenColumn(Object compare1, Object compare2, List<String> columns) throws Exception {
        if (isAnyNull(compare1, compare2)) {
            return Boolean.FALSE;
        }
        PropertyDescriptor[] pds = Introspector.getBeanInfo(compare1.getClass(), Object.class).getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (columns.contains(pd.getName())) {
                if (!Objects.equals(pd.getReadMethod().invoke(compare1), pd.getReadMethod().invoke(compare2))) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;

    }

    /**
     * 根据属性名设置属性值
     *
     * @param fieldName 属性名
     * @param object    对象
     */
    public static void setProperty(Object object, String fieldName, String value) {
        try {
            // 获取obj类的字节文件对象
            Class c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            if (f.getType() == Integer.class) {
                f.set(object, Integer.parseInt(value));
            } else if (f.getType() == LocalDateTime.class) {
                f.set(object, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                f.set(object, value);
            }
        } catch (Exception e) {
            return;
        }
    }
}