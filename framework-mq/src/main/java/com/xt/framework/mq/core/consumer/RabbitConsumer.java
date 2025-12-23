package com.xt.framework.mq.core.consumer;

import com.rabbitmq.client.Channel;
import com.xt.framework.mq.core.RabbitConstants;
import com.xt.framework.mq.core.RabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RabbitConsumer {
    /**
     * 简单模式的消费者
     *
     * @param message 消息
     * @param c       通道
     * @param msg     消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.SIMPLE_QUEUE))
    public void simple(Message message, Channel c, RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        //消息的唯一标识符，单调递增
        long tag = properties.getDeliveryTag();
        log.info("简单模式的消费者收到:{}", msg);
        //由于在yml设置手动回执，此处需要手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag, false);
    }

    /**
     * 工作模式的消费者1
     *
     * @param message 消息
     * @param c       通道
     * @param msg     消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.WORK_QUEUE))
    public void work1(Message message, Channel c, RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        log.info("工作模式的消费者1收到:{}", msg);
        //手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag, false);
    }

    /**
     * 工作模式的消费者2
     *
     * @param message 消息
     * @param c       通道
     * @param msg     消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.WORK_QUEUE))
    public void work2(Message message, Channel c, RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        log.info("工作模式的消费者2收到:{}", msg);
        //手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag, false);
    }

    /**
     * 发布订阅模式方法1的消费者1
     *
     * @param message 消息
     * @param c       通道
     * @param msg     消息内容
     * @throws IOException 异常
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange = @Exchange(name = RabbitConstants.FANOUT_EXCHANGE, declare = "false")//declare = "false"：生产者已定义交换机，此处不再声明交换机
    ))
    public void fanout1(Message message, Channel c, RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        log.info("发布订阅模式方法1的消费者1收到:{}", msg);
        //手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag, false);
    }

    /**
     * 发布订阅模式方法1的消费者2
     *
     * @param message 消息
     * @param c       通道
     * @param msg     消息内容
     * @throws IOException 异常
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange = @Exchange(name = RabbitConstants.FANOUT_EXCHANGE, declare = "false")//declare = "false"：生产者已定义交换机，此处不再声明交换机
    ))
    public void fanout2(Message message, Channel c, RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        log.info("发布订阅模式方法1的消费者2收到:{}", msg);
        //手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag, false);
    }

    /**
     * 发布订阅模式方法2的消费者1
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.FANOUT_QUEUE1))
    public void logs1(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        log.info("发布订阅模式方法2的消费者1收到:{}",msg);
        //手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag,false);
    }

    /**
     * 发布订阅模式方法2的消费者2
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.FANOUT_QUEUE2))
    public void logs2(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        log.info("发布订阅模式方法2的消费者2收到:{}",msg);
        //手动回执，不批量签收,回执后才能处理下一批消息
        c.basicAck(tag,false);
    }

    /**
     * 路由式方法1的消费者1
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange =@Exchange(name=RabbitConstants.ROUTE_EXCHANGE,declare = "false"),//declare = "false"：生产者已定义交换机，此处不再声明交换机
            key = {RabbitConstants.ROUTE_KEY_ERROR}//路由键
    ))
    public void route1(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("路由模式方法1的消费者1收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 路由式方法1的消费者2
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange =@Exchange(name=RabbitConstants.ROUTE_EXCHANGE,declare = "false"),//declare = "false"：生产者已定义交换机，此处不再声明交换机
            key = {RabbitConstants.ROUTE_KEY_INFO,RabbitConstants.ROUTE_KEY_DEBUG}//路由键
    ))
    public void route2(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("路由模式方法1的消费者2收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 路由模式方法2的消费者1
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.ROUTE_QUEUE1))
    public void direct1(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("路由模式方法2的消费者1收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 路由模式方法2的消费者2
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.ROUTE_QUEUE2))
    public void direct2(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("路由模式方法2的消费者2收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }
    /**
     * 主题模式方法1的消费者1
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange =@Exchange(name=RabbitConstants.TOPIC_EXCHANGE,type = ExchangeTypes.TOPIC),//declare = "false"：生产者已定义交换机，此处不再声明交换机
            key = {"*.error.*"}
    ))
    public void theme1(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("主题模式方法1的消费者1收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 主题模式方法1的消费者2
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange =@Exchange(name=RabbitConstants.TOPIC_EXCHANGE,type = ExchangeTypes.TOPIC),//declare = "false"：生产者已定义交换机，此处不再声明交换机
            key = {"#.info","debug.#"}
    ))
    public void theme2(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("主题模式方法1的消费者2收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 主题模式方法2的消费者1
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.TOPIC_QUEUE1))
    public void topic1(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("主题模式方法2的消费者1收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 主题模式方法2的消费者2
     * @param message 消息
     * @param c 通道
     * @param msg 消息内容
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.TOPIC_QUEUE2))
    public void topic2(Message message,Channel c,RabbitMessage msg) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String routingKey = properties.getReceivedRoutingKey();
        log.info("主题模式方法2的消费者2收到:{},路由键:{}",msg,routingKey);
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }


    /**
     * 头部模式方法1的消费者1
     * @param message 消息
     * @param c 通道
     * @throws IOException 异常
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange =@Exchange(name=RabbitConstants.HEADER_EXCHANGE,type = ExchangeTypes.HEADERS),//declare = "false"：生产者已定义交换机，此处不再声明交换机
            arguments = {@Argument(name = "type",value = "ok"),@Argument(name = "status",value = "200"),@Argument(name =
                    "x-match",value = "all")}
    ))
    public void header11(Message message,Channel c) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        log.info("头部模式方法1的消费者1收到:{}",new String(message.getBody()));
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 头部模式方法1的消费者2
     * @param message 消息
     * @param c 通道
     * @throws IOException 异常
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue,//这里定义随机队列,默认属性: 随机命名,非持久,排他,自动删除
            exchange =@Exchange(name=RabbitConstants.HEADER_EXCHANGE,type = ExchangeTypes.HEADERS),//declare = "false"：生产者已定义交换机，此处不再声明交换机
            arguments = {@Argument(name = "type",value = "error"),@Argument(name = "status",value = "500"),@Argument(name =
            "x-match",value = "any")}
    ))
    public void header12(Message message,Channel c) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        log.info("头部模式方法1的消费者2收到:{}",new String(message.getBody()));
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }


    /**
     * 头部模式方法2的消费者1
     * @param message 消息
     * @param c 通道
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.HEADER_QUEUE1))
    public void header1(Message message,Channel c) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        log.info("头部模式方法2的消费者1收到:{}",new String(message.getBody()));
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }

    /**
     * 头部模式方法2的消费者2
     * @param message 消息
     * @param c 通道
     * @throws IOException 异常
     */
    //使用queuesToDeclare属性，如果不存在则会创建队列,注：此处声明的队列要和生产者属性保持一致
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitConstants.HEADER_QUEUE2))
    public void header2(Message message,Channel c) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        log.info("头部模式方法2的消费者2收到:{}",new String(message.getBody()));
        //手动回执，不批量签收,回执后才能处理下一批消息
        long tag = properties.getDeliveryTag();
        c.basicAck(tag,false);
    }





}
