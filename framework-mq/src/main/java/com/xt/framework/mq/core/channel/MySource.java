package com.xt.framework.mq.core.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author tao.xiong
 * @Description 自定义发射信道
 * @Date 2022/7/18 17:23
 */
public interface MySource {
    /**
     * @return 自定义发射信道
     */
    @Output("myOutput")
    MessageChannel myOutput();
}
