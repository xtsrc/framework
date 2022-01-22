package com.xt.framwork.common.core.aliyun;

import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;

import javax.annotation.PostConstruct;

/**
 * @author tao.xiong
 * @Description 阿里云服务
 * @Date 2021/8/18 10:31
 */
public class DemoAliYunApi extends AliYunApi {
    @PostConstruct
    public void initRequest() {
        this.init(super.getHttpParam());
        this.init(super.getHttpsParam());
    }

    @Override
    public void init(HttpClientBuilderParams httpClientBuilderParams) {
        httpClientBuilderParams.setHost(DOCTOR_HOST);
        super.init(httpClientBuilderParams);
    }
}
