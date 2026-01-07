package com.xt.framework.mq.core.producer;

import com.google.common.collect.Lists;
import com.xt.framework.mq.core.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/msg")
@Slf4j
public class KafkaProducer {


    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public String sendMsg() {
        kafkaTemplate.send(KafkaConstants.TEST_TOPIC, 0, "key", "this is a message!")
                .addCallback(success->log.info("send success"),
                        failure->{log.error("send failure :{}",failure.getMessage());});
        return "send success";
    }
    @GetMapping("/batchSend")
    public String batchSendMsg() {
        List<String> msgList= Lists.newArrayList("msg1","msg2","msg3");
        for (String msg:msgList){
            kafkaTemplate.send(KafkaConstants.TEST_TOPIC, 0, "key", msg)
                    .addCallback(success->log.info("send success"),
                            failure->{log.error("send failure :{}",failure.getMessage());});
        }
        return "send success";
    }
    @GetMapping("/batchSendMsgByKey")
    public String batchSendMsgByKey() {
        List<String> msgList= Lists.newArrayList("msg1","msg2","msg3");
        for (String msg:msgList){
            kafkaTemplate.send(KafkaConstants.TEST_TOPIC,  "key"+msg, msg)
                    .addCallback(success->log.info("send success"),
                            failure->{log.error("send failure :{}",failure.getMessage());});
        }
        return "send success";
    }
}
