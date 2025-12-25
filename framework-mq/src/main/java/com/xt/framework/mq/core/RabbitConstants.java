package com.xt.framework.mq.core;

public class RabbitConstants {
    /**************************基础收发消息模式*******************************/
    public final static String SIMPLE_QUEUE = "simpleQueue";
    public final static String WORK_QUEUE = "workQueue";
    public final static String FANOUT_EXCHANGE = "fanout_exchange";
    public final static String FANOUT_EXCHANGE_LOG = "fanout_log_exchange";
    public final static String FANOUT_QUEUE1 = "fanout_queue1";
    public final static String FANOUT_QUEUE2 = "fanout_queue2";
    public final static String ROUTE_EXCHANGE = "route_exchange";
    public final static String ROUTE_EXCHANGE_DIRECT = "route_direct_exchange";
    public final static String ROUTE_QUEUE1 = "route_queue1";
    public final static String ROUTE_QUEUE2 = "route_queue2";
    public final static String ROUTE_KEY_ERROR = "error";
    public final static String ROUTE_KEY_INFO = "info";
    public final static String ROUTE_KEY_DEBUG = "debug";
    public final static String TOPIC_EXCHANGE = "topic_exchange";
    public final static String TOPIC_EXCHANGE2 = "topic_exchange2";
    public final static String TOPIC_QUEUE1 = "topic_queue1";
    public final static String TOPIC_QUEUE2 = "topic_queue2";
    public final static String HEADER_EXCHANGE = "header_exchange";
    public final static String HEADER_EXCHANGE2 = "header_exchange_2";
    public final static String HEADER_QUEUE1 = "header_queue1";
    public final static String HEADER_QUEUE2 = "header_queue2";
    /************************************DLX*******************************/
    //正常队列
    public static final String NORMAL_QUEUE= "normal.queue";
    public static final String NORMAL_EXCHANGE = "normal.exchange";
    public static final String NORMAL_ROUTE_KEY = "normal";
    //延时队列
    public static final String DELAYED_QUEUE= "delayed.queue";
    public static final String DELAYED_ROUTE_KEY = "delayed";
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final Integer MAX_RETRY_COUNT= 3;
    public static final String DELAYED_DL_QUEUE= "delayed.dl.queue";
    public static final String DELAYED_DL_ROUTE_KEY = "delayed.dlrk";
    //死信队列
    public static final String DL_QUEUE= "dl.queue";
    public static final String DL_EXCHANGE = "dl.exchange";
    public static final String DL_ROUTE_KEY = "dlrk";
}
