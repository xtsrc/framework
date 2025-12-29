package com.xt.framework.mq.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitTemplateConfig {

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        // 设置mandatory为true，当找不到队列时，broker会调用basic.return方法将消息返还给生产者
        rabbitTemplate.setMandatory(true);

        // 设置确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息已经到达Exchange");
            } else {
                log.info("消息没有到达Exchange");
            }
            if (correlationData != null) {
                log.info("相关数据：" + correlationData);
            }
            if (cause != null) {
                log.info("原因：" + cause);
            }
        });

        // 设置返回回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息无法到达队列时触发");
            log.info("ReturnCallback:     " + "消息：" + message);
            log.info("ReturnCallback:     " + "回应码：" + replyCode);
            log.info("ReturnCallback:     " + "回应信息：" + replyText);
            log.info("ReturnCallback:     " + "交换机：" + exchange);
            log.info("ReturnCallback:     " + "路由键：" + routingKey);
        });
        return rabbitTemplate;
    }
}
