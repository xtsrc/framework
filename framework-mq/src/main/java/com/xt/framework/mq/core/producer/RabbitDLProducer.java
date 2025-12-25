package com.xt.framework.mq.core.producer;

import com.xt.framework.mq.core.RabbitConstants;
import com.xt.framework.mq.core.RabbitMessage;
import com.xt.framwork.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RabbitDLProducer {
    @Resource
    private AmqpTemplate rabbitTemplate;

    /**
     * 拒绝消费进入死信队列
     *
     * @param message 退回消息
     */
    public void normal(String message) {
        rabbitTemplate.convertAndSend(RabbitConstants.NORMAL_EXCHANGE, RabbitConstants.NORMAL_ROUTE_KEY, JsonUtils.serialize(new RabbitMessage(message, ExchangeTypes.DIRECT)));
    }

    /**
     * 消息延时,重写MessagePostProcessor
     * 不监听延时队列，消息到期后进入死信队列，实现延时逻辑
     *
     * @param message 延时消息
     */
    public void sendDelayedMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitConstants.NORMAL_EXCHANGE, RabbitConstants.DELAYED_ROUTE_KEY
                , JsonUtils.serialize(new RabbitMessage(message, ExchangeTypes.DIRECT))
                , msg -> {
                    // 设置消息的 TTL（单位：毫秒）
                    msg.getMessageProperties().setExpiration("2000");
                    return msg;
                });
    }

    /**
     *
     * @param message 插件实现的延时消息
     */
    public void sendDelayedMessageByPlugins(String message) {
        log.info("发送延时消息");
        rabbitTemplate.convertAndSend(RabbitConstants.DELAYED_EXCHANGE, RabbitConstants.NORMAL_ROUTE_KEY
                , JsonUtils.serialize(new RabbitMessage(message, ExchangeTypes.DIRECT)),msg->{
                    msg.getMessageProperties().setDelay(5000);
                    return msg;
                });
    }

    /**
     * 队列达到最大长度进入死信队列
     *
     * @param message 超限消息
     */
    public void sendOutOfRangeMessage(String message) {
        for (int i = 0; i < 15; i++) {
            rabbitTemplate.convertAndSend(RabbitConstants.NORMAL_EXCHANGE, RabbitConstants.DELAYED_ROUTE_KEY, JsonUtils.serialize(new RabbitMessage(message + i, ExchangeTypes.DIRECT)));
        }
    }
}
