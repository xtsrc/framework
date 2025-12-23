package com.xt.framework.mq.core.producer;

import com.xt.framework.mq.core.RabbitConstants;
import com.xt.framework.mq.core.RabbitMessage;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


@Component
public class RabbitProducer {
    @Resource
    private AmqpTemplate rabbitTemplate;

    public void simpleQueue(String message) {
        rabbitTemplate.convertAndSend(RabbitConstants.SIMPLE_QUEUE, new RabbitMessage(message, ExchangeTypes.SYSTEM));
    }

    public void work() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(RabbitConstants.WORK_QUEUE, new RabbitMessage(i, ExchangeTypes.SYSTEM), message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                //默认消息持久化，设置消息不持久化
                messageProperties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
                return message;
            });
        }
    }
    public void fanout1(String message){
        rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_EXCHANGE,null,new RabbitMessage(message, ExchangeTypes.FANOUT));
    }
    public void fanout2(String message){
        rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_EXCHANGE_LOG,null,new RabbitMessage(message, ExchangeTypes.FANOUT));
    }
    public void route1(String message,String routeKey){
        rabbitTemplate.convertAndSend(RabbitConstants.ROUTE_EXCHANGE,routeKey,new RabbitMessage(message, ExchangeTypes.DIRECT));
    }
    public void route2(String message,String routeKey){
        rabbitTemplate.convertAndSend(RabbitConstants.ROUTE_EXCHANGE_DIRECT,routeKey,new RabbitMessage(message, ExchangeTypes.DIRECT));
    }
    public void topic1(String message,String routeKey){
        rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_EXCHANGE,routeKey,new RabbitMessage(message, ExchangeTypes.TOPIC));
    }
    public void topic2(String message,String routeKey){
        rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_EXCHANGE2,routeKey,new RabbitMessage(message, ExchangeTypes.TOPIC));
    }

    /**
     *
     * @param message 满足队列1的消息
     */
    public void header1(String message){
        //消息属性
        MessageProperties messageProperties=new MessageProperties();
        //设置消息头
        messageProperties.setHeader("type","ok");
        messageProperties.setHeader("status","200");
        //添加消息属性
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(RabbitConstants.HEADER_EXCHANGE2,"",msg);
    }

    /**
     *
     * @param message 满足队列2的消息
     */
    public void header2(String message){
        //消息属性
        MessageProperties messageProperties=new MessageProperties();
        //设置消息头
        messageProperties.setHeader("type","error");
        messageProperties.setHeader("status","500");
        //添加消息属性
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(RabbitConstants.HEADER_EXCHANGE2,"",msg);
    }

    /**
     * @param message 其他不满足条件消息
     */
    public void header3(String message){
        //消息属性
        MessageProperties messageProperties=new MessageProperties();
        //设置消息头
        messageProperties.setHeader("type","未知");
        messageProperties.setHeader("status","444");
        //添加消息属性
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(RabbitConstants.HEADER_EXCHANGE2,"",msg);
    }

    /**
     *
     * @param message 方式一满足消费者2的消息
     */
    public void header11(String message){
        //消息属性
        MessageProperties messageProperties=new MessageProperties();
        //设置消息头
        messageProperties.setHeader("type","ok");
        messageProperties.setHeader("status","200");
        //添加消息属性
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(RabbitConstants.HEADER_EXCHANGE,"",msg);
    }
    /**
     *
     * @param message 方式一满足消费者2的消息
     */
    public void header12(String message){
        //消息属性
        MessageProperties messageProperties=new MessageProperties();
        //设置消息头
        messageProperties.setHeader("type","ok");
        messageProperties.setHeader("status","200");
        //添加消息属性
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(RabbitConstants.HEADER_EXCHANGE,"",msg);
    }
    /**
     *
     * @param message 方式一x-match测试的消息
     */
    public void header13(String message){
        //消息属性
        MessageProperties messageProperties=new MessageProperties();
        //设置消息头
        messageProperties.setHeader("type","未知");
        messageProperties.setHeader("status","200");
        //添加消息属性
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(RabbitConstants.HEADER_EXCHANGE,"",msg);
    }
}
