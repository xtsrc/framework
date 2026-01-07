package com.xt.framework.mq.core.consumer;

import com.xt.framework.mq.core.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaConsumer {

    /*@KafkaListener(topics = KafkaConstants.TEST_TOPIC, groupId = KafkaConstants.XT_GROUP)
    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("single consumer:{}",record);
        //手动提交offset【如果不提交offset，会导致消息重复消费】
        ack.acknowledge();
    }*/
    @KafkaListener(topics = KafkaConstants.TEST_TOPIC, groupId = KafkaConstants.XT_GROUP_BATCH)
    public void batchConsume(List<String> msgList, Acknowledgment ack) {
        log.info("batch consumer:{}",msgList);
        ack.acknowledge();
    }
    @KafkaListener(topics = KafkaConstants.TEST_TOPIC, groupId = KafkaConstants.XT_GROUP_BATCH)
    public void batchConsume2(List<String> msgList, Acknowledgment ack) {
        log.info("batch consumer2:{}",msgList);
        ack.acknowledge();
    }
    @KafkaListener(topics = KafkaConstants.TEST_TOPIC, groupId = KafkaConstants.XT_GROUP_BATCH)
    public void batchConsume3(List<String> msgList, Acknowledgment ack) {
        log.info("batch consumer3:{}",msgList);
        ack.acknowledge();
    }@KafkaListener(topics = KafkaConstants.TEST_TOPIC, groupId = KafkaConstants.XT_GROUP_BATCH)
    public void batchConsume4(List<String> msgList, Acknowledgment ack) {
        log.info("batch consumer4:{}",msgList);
        ack.acknowledge();
    }
    @KafkaListener(topics = KafkaConstants.TEST_TOPIC, groupId = KafkaConstants.XT_GROUP)
    public void batchConsume5(List<String> msgList, Acknowledgment ack) {
        log.info("batch consumer5:{}",msgList);
        ack.acknowledge();
    }
}
