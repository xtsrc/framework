package com.xt.framework.mq.core.config;

import com.xt.framework.mq.core.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    /**
     * 一个生产者一个消费者
     * 消息拿走自动删除
     *
     * @return 简单队列模式
     */
    @Bean
    public Queue simpleQueue() {
        return QueueBuilder.durable(RabbitConstants.SIMPLE_QUEUE)
                .build();
    }

    /**
     * 一个生产者多个消费者
     * 异步处理、平衡负载、每个消息消费一次
     *
     * @return 工作模式队列
     */
    @Bean
    public Queue workQueue() {
        return QueueBuilder.durable(RabbitConstants.WORK_QUEUE).build();
    }

    /**
     * 发布订阅模式：消息广播，多个消费者同时接收
     *
     * @return fanout交换机
     */
    @Bean
    public Exchange fanoutExchange1() {
        return ExchangeBuilder.fanoutExchange(RabbitConstants.FANOUT_EXCHANGE).build();
    }

    /**
     * @param connectionFactory 连接
     * fanout 方法一：生产者创建交换机，消费者创建队列、监听队列
     * @return 创建初始化RabbitAdmin对象
     */
    @Bean
    public RabbitAdmin fanoutRabbitAdmin1(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareExchange(fanoutExchange1());
        return rabbitAdmin;
    }

    /**
     * 定义队列 持久 非排他 非自动删除
     *
     * @return fanout 队列
     */
    @Bean
    public Queue fanoutQueue1() {
        return QueueBuilder.durable(RabbitConstants.FANOUT_QUEUE1).build();
    }

    /**
     * 定义队列 持久 非排他 非自动删除
     *
     * @return fanout 队列
     */
    @Bean
    public Queue fanoutQueue2() {
        return QueueBuilder.durable(RabbitConstants.FANOUT_QUEUE2).build();
    }

    /**
     * 定义扇出交换机 持久  非自动删除
     *
     * @return fanout交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange(RabbitConstants.FANOUT_EXCHANGE_LOG).build();
    }

    /**
     * 将队列1与交换机绑定
     *
     * @return fanout绑定
     */
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    /**
     * 将队列2与交换机绑定
     *
     * @return fanout绑定
     */
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /**
     * fanout方法二：生产者创建交换机与队列，消费者监听队列
     *
     * @param connectionFactory 连接
     * @return 创建初始化RabbitAdmin对象
     */
    @Bean
    public RabbitAdmin fanoutRabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareExchange(fanoutExchange());
        rabbitAdmin.declareQueue(fanoutQueue1());
        rabbitAdmin.declareQueue(fanoutQueue2());
        return rabbitAdmin;
    }

    /**
     * 路由模式：生产者通过direct直流交换机发消息，消费者通过关键字匹配从绑定的队列获得消息
     * 根据不同的routeKey不同的消费者处理不同的消息
     * @return 直流交换机
     */
    @Bean
    public Exchange routeExchange(){
        //持久化 非自动删除
        return ExchangeBuilder.directExchange(RabbitConstants.ROUTE_EXCHANGE).build();
    }

    /**
     * 方式一：生产者创建交换机，消费者创建队列、监听队列
     * @param connectionFactory 连接
     * @return 创建初始化RabbitAdmin对象
     */
    @Bean
    public RabbitAdmin RabbitAdminRoute(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.declareExchange(routeExchange());
        return rabbitAdmin;
    }


        /**
         * 定义队列 持久 非排他 非自动删除
         * @return 路由队列1
         */
        @Bean
        public Queue directQueue1(){
            return  QueueBuilder.durable(RabbitConstants.ROUTE_QUEUE1).build();
        }

        /**
         * 定义队列 持久 非排他 非自动删除
         * @return 路由队列2
         */
        @Bean
        public Queue directQueue2(){
            return  QueueBuilder.durable(RabbitConstants.ROUTE_QUEUE2).build();
        }

        /**
         * 定义路由交换机 持久  非自动删除
         * @return 路由交换机
         */
        @Bean
        public DirectExchange directExchange(){
            return  ExchangeBuilder.directExchange(RabbitConstants.ROUTE_EXCHANGE_DIRECT).build();
        }

        /**
         * 将队列1与交换机绑定，路由键:error
         * @return 绑定
         */
        @Bean
        public Binding directBinding1(){
            return BindingBuilder.bind(directQueue1()).to(directExchange()).with(RabbitConstants.ROUTE_KEY_ERROR);
        }

        /**
         * 将队列2与交换机绑定，路由键:info
         * @return 绑定
         */
        @Bean
        public Binding directBinding2(){
            return BindingBuilder.bind(directQueue2()).to(directExchange()).with(RabbitConstants.ROUTE_KEY_INFO);
        }

        /**
         * 将队列2与交换机绑定，路由键:debug
         * @return 绑定
         */
        @Bean
        public Binding directBinding3(){
            return BindingBuilder.bind(directQueue2()).to(directExchange()).with(RabbitConstants.ROUTE_KEY_DEBUG);
        }

        /**
         * route方法二：生产者创建交换机与队列，消费者监听队列
         *
         * @param connectionFactory 连接
         * @return 创建初始化RabbitAdmin对象
         */
        @Bean
        public RabbitAdmin directRabbitAdmin(ConnectionFactory connectionFactory) {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
            // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
            rabbitAdmin.setAutoStartup(true);
            rabbitAdmin.declareExchange(directExchange());
            rabbitAdmin.declareQueue(directQueue1());
            rabbitAdmin.declareQueue(directQueue2());
            return rabbitAdmin;
        }

    /**
     * 主题模式：生产者通过topic主题交换机发消息，消费者通过特殊的关键字匹配从绑定的队列获得消息
     * 根据不同的routeKey不同的消费者处理不同的消息
     * @return topic主题交换机
     */
    @Bean
    public Exchange topicExchange(){
        //持久化 非自动删除
        return ExchangeBuilder.topicExchange(RabbitConstants.TOPIC_EXCHANGE).build();
    }

    /**
     * 方式一：生产者创建交换机，消费者创建队列、监听队列
     * @param connectionFactory 连接
     * @return 创建初始化RabbitAdmin对象
     */
    @Bean
    public RabbitAdmin RabbitAdminTopic(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.declareExchange(topicExchange());
        return rabbitAdmin;
    }



        /**
         * 定义队列 持久 非排他 非自动删除
         * @return 队列
         */
        @Bean
        public Queue topicQueue1(){
            return  QueueBuilder.durable(RabbitConstants.TOPIC_QUEUE1).build();
        }

        /**
         * 定义队列 持久 非排他 非自动删除
         * @return 队列
         */
        @Bean
        public Queue topicQueue2(){
            return  QueueBuilder.durable(RabbitConstants.TOPIC_QUEUE2).build();
        }

        /**
         * 定义路由交换机 持久  非自动删除
         * @return 交换机
         */
        @Bean
        public TopicExchange topicExchange2(){
            return  ExchangeBuilder.topicExchange(RabbitConstants.TOPIC_EXCHANGE2).build();
        }

        /**
         * 将队列1与交换机绑定，路由键:A.*
         * @return 绑定
         */
        @Bean
        public Binding topicBinding1(){
            return BindingBuilder.bind(topicQueue1()).to(topicExchange2()).with("A.*");
        }

        /**
         * 将队列2与交换机绑定，路由键:#.B
         * @return 绑定
         */
        @Bean
        public Binding topicBinding2(){
            return BindingBuilder.bind(topicQueue1()).to(topicExchange2()).with("#.B");
        }

        /**
         * 将队列2与交换机绑定，路由键:A.B
         * @return 绑定
         */
        @Bean
        public Binding topicBinding3(){
            return BindingBuilder.bind(topicQueue2()).to(topicExchange2()).with("A.B");
        }

        /**
         * 方法二：生产者创建交换机与队列，消费者监听队列
         * @param connectionFactory 连接
         * @return 创建初始化RabbitAdmin对象
         */
        @Bean
        public RabbitAdmin topicRabbitAdmin(ConnectionFactory connectionFactory) {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
            // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
            rabbitAdmin.setAutoStartup(true);
            rabbitAdmin.declareExchange(topicExchange2());
            rabbitAdmin.declareQueue(topicQueue1());
            rabbitAdmin.declareQueue(topicQueue2());
            return rabbitAdmin;
        }

    /**
     * 头部模式：生产者通过header头部交换机发消息，消费者通过头部条件匹配从绑定的队列获得消息
     * 按条件消息入队
     * @return topic主题交换机
     */
    @Bean
    public Exchange headerExchange(){
        //持久化 非自动删除
        return ExchangeBuilder.headersExchange(RabbitConstants.HEADER_EXCHANGE).build();
    }

    /**
     *
     * 方式一：生产者创建交换机，消费者创建队列、监听队列
     * @param connectionFactory 连接
     * @return 创建初始化RabbitAdmin对象
     */
    @Bean
    public RabbitAdmin headerRabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.declareExchange(headerExchange());
        return rabbitAdmin;
    }

    /**
     * 定义队列 持久 非排他 非自动删除
     * @return 队列
     */
    @Bean
    public Queue headerQueue1(){
        return  QueueBuilder.durable(RabbitConstants.HEADER_QUEUE1).build();
    }

    /**
     * 定义队列 持久 非排他 非自动删除
     * @return 队列
     */
    @Bean
    public Queue headerQueue2(){
        return  QueueBuilder.durable(RabbitConstants.HEADER_QUEUE2).build();
    }

    /**
     * 定义路由交换机 持久  非自动删除
     * @return 交换机
     */
    @Bean
    public HeadersExchange headerExchange2(){
        return  ExchangeBuilder.headersExchange(RabbitConstants.HEADER_EXCHANGE2).build();
    }

    /**
     * 将队列1与交换机绑定，条件
     * @return 绑定
     */
    @Bean
    public Binding headerBinding1(HeadersExchange headerExchange2,Queue headerQueue1){
        Map<String,Object> map=new HashMap<>();
        map.put("type","ok");
        map.put("status","200");
        return BindingBuilder.bind(headerQueue1).to(headerExchange2)
                .whereAll(map).match();
    }

    /**
     * 将队列2与交换机绑定，条件
     * @return 绑定
     */
    @Bean
    public Binding headerBinding2(HeadersExchange headerExchange2,Queue headerQueue2){
        Map<String,Object> map=new HashMap<>();
        map.put("type","error");
        map.put("status","500");
        return BindingBuilder.bind(headerQueue2).to(headerExchange2)
                .whereAny(map).match();
    }

    /**
     *
     * @param connectionFactory 连接
     * @return 创建初始化RabbitAdmin对象
     */
    @Bean
    public RabbitAdmin headerRabbitAdmin2(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareExchange(headerExchange2());
        rabbitAdmin.declareQueue(headerQueue1());
        rabbitAdmin.declareQueue(headerQueue2());
        return rabbitAdmin;
    }



}
