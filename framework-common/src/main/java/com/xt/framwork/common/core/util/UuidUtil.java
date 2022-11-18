package com.xt.framwork.common.core.util;

import java.util.UUID;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/6/29 11:17
 */
public class UuidUtil {
    private UuidUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getUuid() {
        return uuid();
    }

    protected static String uuid() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
