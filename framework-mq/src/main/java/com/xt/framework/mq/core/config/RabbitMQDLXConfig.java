package com.xt.framework.mq.core.config;

import com.xt.framework.mq.core.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQDLXConfig {

    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable(RabbitConstants.NORMAL_QUEUE)
                .deadLetterExchange(RabbitConstants.DL_EXCHANGE)
                .deadLetterRoutingKey(RabbitConstants.DL_ROUTE_KEY)
                .maxLength(10)//队列长度设置，超过了进入死信
                .build();
    }
    @Bean
    public DirectExchange normalExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.NORMAL_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding normalBinding() {
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with(RabbitConstants.NORMAL_ROUTE_KEY);
    }

    /**
     *
     * @return 死信队列实现的延时队列
     */
    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(RabbitConstants.DELAYED_QUEUE)
                .deadLetterExchange(RabbitConstants.DL_EXCHANGE)
                .deadLetterRoutingKey(RabbitConstants.DELAYED_DL_ROUTE_KEY)
                .ttl(5*1000) //消息ttl和队列ttl都可设置，两者取最小的触发
                //.withArgument("x-message-ttl","10000")
                .maxLength(10)
                .build();
    }
    @Bean
    public DirectExchange delayedExchange(){
        return ExchangeBuilder.directExchange(RabbitConstants.DELAYED_EXCHANGE).delayed().build();
    }

    /**
     * 插件实现的延时队列
     * @return 延时交换机与普通队列的绑定
     */
    @Bean
    public Binding normalDelayedBinding() {
        return BindingBuilder.bind(normalQueue()).to(delayedExchange()).with(RabbitConstants.NORMAL_ROUTE_KEY);
    }

    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(delayedQueue()).to(normalExchange()).with(RabbitConstants.DELAYED_ROUTE_KEY);
    }

    @Bean
    public Queue dlQueue() {
        return QueueBuilder.durable(RabbitConstants.DL_QUEUE).build();
    }

    @Bean
    public DirectExchange dlExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DL_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding dlBinding() {
        return BindingBuilder.bind(dlQueue()).to(dlExchange()).with(RabbitConstants.DL_ROUTE_KEY);
    }
    @Bean
    public Queue delayedDLQueue() {
        return QueueBuilder.durable(RabbitConstants.DELAYED_DL_QUEUE).build();
    }


    @Bean
    public Binding delayedDLBinding() {
        return BindingBuilder.bind(delayedDLQueue()).to(dlExchange()).with(RabbitConstants.DELAYED_DL_ROUTE_KEY);
    }

}
