package com.xt.framework.mq.core.producer;

/*import com.xt.framework.mq.core.channel.MySource;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;

import javax.annotation.Resource;*/


/**
 * @author tao.xiong
 * @Description kafka 发送
 * Source（发射器）: 一个接口类，内部定义了一个输出管道，例如定义一个输出管道 @output（"XXOO"）。说明这个发射器将会向这个管道发射数据。
 * 消息分区：一个或者多个生产者应用实例给多个消费者应用实例发送消息并确保相同特征的数据被同一消费者实例处理
 * @Date 2022/7/18 17:14
 */
/*@EnableBinding(Source.class)*/
/*@EnableBinding(MySource.class)*/
public class SendService {
    /*@Resource
    private Source source;
    @Resource
    private MySource mySource;

    public void sendMsg(String msg) {
        source.output().send(MessageBuilder.withPayload(msg).build());
    }
    public void sendMyMsg(String msg) {
        mySource.myOutput().send(MessageBuilder.withPayload(msg).build());
    }*/
}


