package com.xt.framework.mq;

import com.xt.framework.mq.core.producer.PulsarProducer;
import io.github.majusko.pulsar.PulsarMessage;
import log.EnableAutoLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@EnableDiscoveryClient
@RestController
@SpringBootApplication()
@EnableAutoLog
public class PulsarApplication {
    @Resource
    private PulsarProducer pulsarProducer;

    @GetMapping("sendMsg")
    public void send(@RequestParam("msg") String msg) {
        PulsarMessage<String> message = new PulsarMessage<>();
        message.setKey(msg);
        pulsarProducer.send(message);
    }

    public static void main(String[] args) {
        SpringApplication.run(PulsarApplication.class, args);
    }
}
