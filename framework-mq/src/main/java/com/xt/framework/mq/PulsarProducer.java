package com.xt.framework.mq;

import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 生产者
 * @Date 2021/8/30 15:33
 */
@Component
public class PulsarProducer {
    @Resource
    private PulsarTemplate<PulsarMessage<String>> template;

    public void send(PulsarMessage<String> pulsarMessage) {
        try {
            template.send("bootTopic", pulsarMessage);
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
