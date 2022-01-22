package com.xt.framework.db.mysql.util;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * @author tao.xiong
 * @Description 数据库加解密
 * @Date 2022/1/20 17:36
 */
public class EncryptUtil {
    public static void main(String[] args) {
        try {
            String password = "root";
            String[] arr = ConfigTools.genKeyPair(512);

            // System.out.println("privateKey:" + arr[0]);
            System.out.println("publicKey:" + arr[1]);
            System.out.println("password:" + ConfigTools.encrypt(arr[0], password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
