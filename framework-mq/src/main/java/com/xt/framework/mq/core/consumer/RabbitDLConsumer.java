package com.xt.framework.mq.core.consumer;

import com.rabbitmq.client.Channel;
import com.xt.framework.mq.core.RabbitConstants;
import com.xt.framework.mq.core.RabbitMessage;
import com.xt.framwork.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class RabbitDLConsumer {
    @Resource
    private AmqpTemplate rabbitTemplate;
    private int retry=0;

    /**
     * 自动ack,重试会独占线程，需要concurrency控制并发数量
     * 手动ACK 会永远处于Unacked状态，不会进入到死信队列
     * 重试和MQ无关，消费端重试，可以引入其他重试组件
     * @param message 消息
     * @param channel 渠道
     * @throws Exception 异常
     */
    @Retryable(
            value = {Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)
    )
    @RabbitListener(queues = RabbitConstants.NORMAL_QUEUE,concurrency = "3")
    public void handleMessage(Message message, Channel channel) throws Exception {
        //消费者逻辑
        long deliverTag = message.getMessageProperties().getDeliveryTag();
        retry++;
        try {
            log.info("普通队列收到消息:{}", JsonUtils.parseJson(new String(message.getBody()), RabbitMessage.class));
            if(retry<3){
                throw new RuntimeException();
            }
            retry=0;
            channel.basicAck(deliverTag,false);
        } catch (Exception e) {
            log.error("普通队列消息错误");
            //否定确认requeue为false，则变成死信队列
            //channel.basicNack(deliverTag, false, false);
            //重试手动模式下需要抛出异常
            throw e;
        }
    }
    @RabbitListener(queues = RabbitConstants.NORMAL_QUEUE,concurrency = "3")
    public void handleDelayedMessage(Message message, Channel channel) throws IOException {
        log.info("收到延时消息：{}",JsonUtils.deserialize(message.getBody(),RabbitMessage.class));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 达到最大重试次数
     * @param e 错误
     * @param message 同Retryable message 可选
     * @param channel 同Retryable channel 可选
     */
    @Recover
    public void recoverMessage(Exception e,Message message, Channel channel)  {
        long deliverTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("消息重试失败进入死信队列");
            channel.basicNack(deliverTag, false, false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    @RabbitListener(queues = RabbitConstants.DL_QUEUE)
    public void dlxHandleMessage(Message message, Channel channel) throws IOException {
        log.info("死信队列收到消息：{}", JsonUtils.deserialize(message.getBody(), RabbitMessage.class));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 利用死信队列实现延时和重试逻辑
     * @param message 延时队列超过时间转死信队列消息
     * @param channel 渠道
     * @throws IOException 异常
     */
    @RabbitListener(queues = RabbitConstants.DELAYED_DL_QUEUE)
    public void delayedDLMessage(Message message, Channel channel) throws IOException {
        try {
            // 处理消息的业务逻辑，如果发生异常，将消息重试
            throw new RuntimeException("Simulating an exception during message processing");
        } catch (Exception e) {
            RabbitMessage rabbitMessage = JsonUtils.deserialize(message.getBody(), RabbitMessage.class);
            Long retryCount= rabbitMessage.getRetry();
            log.info("延时队列监听死信实现重试，message:{},count:{}",rabbitMessage.getMessageId(),retryCount);
            if (retryCount != null && retryCount < RabbitConstants.MAX_RETRY_COUNT) {
                // 如果重试次数小于最大重试次数，则将消息重新发送到原队列
                // 在实际应用中，可能需要根据业务需求进行更复杂的重试逻辑
                // 这里使用默认的交换机和路由键，发送到原队列
                // 实际应用中，可能需要根据具体情况进行定制化处理
                rabbitMessage.setRetry(retryCount+1);
                rabbitTemplate.convertAndSend(RabbitConstants.NORMAL_EXCHANGE, RabbitConstants.DELAYED_ROUTE_KEY,JsonUtils.serialize(rabbitMessage));
            } else {
                // 超过最大重试次数，存入错误队列
                rabbitTemplate.convertAndSend(RabbitConstants.DL_EXCHANGE, RabbitConstants.DL_ROUTE_KEY,JsonUtils.serialize(rabbitMessage));
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
