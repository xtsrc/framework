package com.xt.framework.mq;

/*import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;*/
//import com.xt.framework.mq.core.producer.PulsarProducer;
import com.xt.framework.mq.core.RabbitConstants;
import com.xt.framework.mq.core.producer.RabbitConfirmProducer;
import com.xt.framework.mq.core.producer.RabbitDLProducer;
import com.xt.framework.mq.core.producer.RabbitProducer;
//import io.github.majusko.pulsar.PulsarMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@EnableDiscoveryClient
@RestController
@SpringBootApplication()
/*@EnableGlobalConfig*/
@EnableRetry
public class MqApplication {
   /* @Resource
    private PulsarProducer pulsarProducer;*/
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private RabbitDLProducer rabbitDLProducer;
    @Resource
    private RabbitConfirmProducer rabbitConfirmProducer;

    @GetMapping("sendMsg")
    public void send(@RequestParam("msg") String msg) {
        /*PulsarMessage<String> message = new PulsarMessage<>();
        message.setKey(msg);
        pulsarProducer.send(message);*/
        rabbitProducer.simpleQueue(msg);
        rabbitProducer.work();
        rabbitProducer.fanout1(msg);
        rabbitProducer.fanout2(msg);
        rabbitProducer.route1("error1"+msg, RabbitConstants.ROUTE_KEY_ERROR);
        rabbitProducer.route1("info1"+msg,RabbitConstants.ROUTE_KEY_INFO);
        rabbitProducer.route2("error2"+msg, RabbitConstants.ROUTE_KEY_ERROR);
        rabbitProducer.route2("info2"+msg,RabbitConstants.ROUTE_KEY_INFO);
        rabbitProducer.topic1("error1"+msg, "xx.error.xxx");
        rabbitProducer.topic1("info1"+msg, RabbitConstants.ROUTE_KEY_INFO);
        rabbitProducer.topic2("AB2"+msg, "A.1");
        rabbitProducer.topic2("AB2"+msg, "1.B");
        rabbitProducer.header11(msg+"ok");
        rabbitProducer.header12(msg+"error");
        rabbitProducer.header13(msg+"其他");
        rabbitProducer.header1(msg+"ok");
        rabbitProducer.header2(msg+"error");
        rabbitProducer.header3(msg+"其他");

    }
    @GetMapping("dl")
    public void dl(@RequestParam("msg") String msg) {
        rabbitDLProducer.normal(msg);
    }
    @GetMapping("dl/delayed")
    public void delayed(@RequestParam("msg") String msg) {
        rabbitDLProducer.sendDelayedMessage(msg);
    }
    @GetMapping("dl/delayedByPlugins")
    public void delayedByPlugins(@RequestParam("msg") String msg) {
        rabbitDLProducer.sendDelayedMessageByPlugins(msg);
    }
    @GetMapping("dl/outRange")
    public void outRange(@RequestParam("msg") String msg) {
        rabbitDLProducer.sendOutOfRangeMessage(msg);
    }
    @GetMapping("cf/noExchange")
    public void noExchange(@RequestParam("msg") String msg) {
        rabbitConfirmProducer.noExchange(msg);
    }
    @GetMapping("cf/toExchange")
    public void toExchange(@RequestParam("msg") String msg) {
        rabbitConfirmProducer.toExchange(msg);
    }


    public static void main(String[] args) {
        SpringApplication.run(MqApplication.class, args);
    }
}
