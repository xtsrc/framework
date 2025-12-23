/*package com.xt.framework.mq.core.consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;


*//**
 * @author tao.xiong
 * @Description kafka消费
 * Sink（接收器）:一个接口类，内部定义了一个输入管道，例如定义一个输入管道 @input（"XXOO"）。说明这个接收器将会从这个管道接收数据。
 * Binder（绑定器）:用于与管道进行绑定。Binder将于消息中间件进行关联。@ EnableBinding （Source.class/Sink.class）。@EnableBinding（）里面是可以定义多个发射器/接收器
 * 消息分组:同一个组内会发生竞争关系，只有其中一个可以消费
 * @Date 2022/7/18 17:17
 *//*
*//*@EnableBinding(Sink.class)*//*
@EnableBinding(MySink.class)
public class ReceiveService {
    @StreamListener(Sink.INPUT)
    public void receive(Object payload){
        System.out.println(payload);
    }

    @StreamListener("myInput")
    public void receiveMy(Object payload){
        System.out.println(payload);
    }
}*/
