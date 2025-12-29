package com.xt.framework.mq.core.producer;

import com.xt.framework.mq.core.RabbitConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RabbitConfirmProducer {
    @Resource
    private AmqpTemplate rabbitTemplate;
    public void noExchange(String msg) {
        rabbitTemplate.convertAndSend("noExchange", "noExchange", msg);
    }
    public void toExchange(String msg) {
        rabbitTemplate.convertAndSend(RabbitConstants.NORMAL_EXCHANGE, "xxx.xxx.xxx", msg);
    }
}
