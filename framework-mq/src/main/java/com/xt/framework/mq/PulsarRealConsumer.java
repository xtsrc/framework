package com.xt.framework.mq;

import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tao.xiong
 * @Description 消息消费者
 * @Date 2021/8/30 15:35
 */
@Slf4j
@Component
public class PulsarRealConsumer {

    @PulsarConsumer(topic="bootTopic", clazz= PulsarMessage.class)
    public void consume(PulsarMessage<String> pulsarMessage) {
        log.info("PulsarRealConsumer consume id:{},content:{}", pulsarMessage.getMessageId(), pulsarMessage.getValue());
    }
}
