package com.xt.framework.demo.designmode.singleton;

import org.springframework.web.client.RestTemplate;

/**
 * @author tao.xiong
 * @Description 单链接资源
 * @Date 2021/9/10 22:52
 */
public interface SingleConnectSource {
    String URL = "https://test?token=%s";
    RestTemplate REST_TEMPLATE = new RestTemplate();

    /**
     * 下载
     * @param token 请求
     * @return 资源
     */
    String download(String token);

    /**
     * 上传
     * @param token 请求
     * @param data 资源
     */
    void upload(String token,String data);
}
