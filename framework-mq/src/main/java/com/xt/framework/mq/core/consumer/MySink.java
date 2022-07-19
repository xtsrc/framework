package com.xt.framework.mq.core.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author tao.xiong
 * @Description 自定义接收信道
 * @Date 2022/7/18 17:26
 */
public interface MySink {
    /**
     * @return 自定义接收信道
     */
    @Input("myInput")
    SubscribableChannel myInput();
}

