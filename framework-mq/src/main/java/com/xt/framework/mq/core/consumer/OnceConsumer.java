package com.xt.framework.mq.core.consumer;

import com.rabbitmq.client.Channel;
import com.xt.framework.db.api.IRedisApi;
import com.xt.framework.mq.core.RabbitConstants;
import com.xt.framework.mq.core.RabbitMessage;
import com.xt.framwork.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class OnceConsumer {
    /**
     * 手动 Ack + 记录日志 + 监控重复次数
     */
    @Resource
    private IRedisApi IRedisApi;

    @RabbitListener(queues = RabbitConstants.NORMAL_QUEUE)
    public void handleMessage(Message message, Channel channel) throws Exception {
        long deliverTag = message.getMessageProperties().getDeliveryTag();
        try {
            RabbitMessage rabbitMessage = JsonUtils.parseJson(new String(message.getBody()), RabbitMessage.class);
            assert rabbitMessage != null;
            log.info("普通队列收到消息:{}", rabbitMessage);
            Long messageId = rabbitMessage.getMessageId();
            String key = "message:key:" + messageId;
            Boolean exist = IRedisApi.setIfAbsent(key, "1", 30L);
            //redis防重，高QPS 但低一致性
            if (Boolean.TRUE.equals(exist)) {
                try {
                    done(messageId);
                    // 4. 业务成功，手动Ack（告诉Broker消息处理完了，别再投了）
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (Exception e) {
                    // 5. 业务失败，根据情况处理：是重试还是丢死信队列
                    log.error("处理订单支付消息失败，orderNo={}", messageId, e);
                    // 比如：如果是数据库临时不可用，就拒绝消息让Broker重试；如果是业务错误（比如订单不存在），就直接Ack丢了
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                }
            } else {
                // 6. 防重命中，说明已经处理过了，直接Ack
                log.warn("订单消息已重复消费，orderNo={}", messageId);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            log.error("普通队列消息错误");
            //否定确认requeue为false，则变成死信队列
            channel.basicNack(deliverTag, false, false);
        }
    }

    /**
     * 业务逻辑去重
     *
     * @param messageId 业务id
     * @return
     */
    protected boolean done(Long messageId) {
        //查库判断重复
        List<Long> messageIds = Lists.newArrayList();
        //状态机、责任链判断
        Boolean judge = false;
        if (messageIds.contains(messageId) || !judge) {
            return false;
        } else {
            //业务逻辑
            return true;
        }
    }

    @Transactional // 用事务保证“插防重表”和“业务处理”原子性，低 QPS 但高一致性
    public void handleTransferMsg(String msgUniqueId, String msgContent) {
        try {
            // 1. 插入防重表：如果报唯一约束异常，说明已处理
           /* MsgProcessRecord record = new MsgProcessRecord();
            record.setMsgUniqueId(msgUniqueId);
            record.setMsgContent(msgContent);
            msgProcessMapper.insert(record);*/

            // 2. 执行转账业务（核心逻辑）
            //transferService.doTransfer(msgContent);

            // 3. 更新防重表状态为“处理成功”
           /* record.setProcessStatus(1);
            msgProcessMapper.updateById(record);*/
        } catch (DuplicateKeyException e) {
            // 4. 唯一约束冲突，说明已处理过
            log.warn("转账消息已重复消费，msgUniqueId={}", msgUniqueId);
        } catch (Exception e) {
            // 5. 业务失败，更新状态为“处理失败”，后续人工排查
            log.error("处理转账消息失败，msgUniqueId={}", msgUniqueId, e);
            /*record.setProcessStatus(2);
            msgProcessMapper.updateById(record);*/
        }
    }

}
