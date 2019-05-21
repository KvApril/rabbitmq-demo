package com.kv.mqdemo.rabbitmq.consumer;

import com.alibaba.fastjson.JSON;
import com.kv.mqdemo.rabbitmq.MQConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DeadQueueConsumer {
    private final Logger logger = LoggerFactory.getLogger(DeadQueueConsumer.class);

//    @RabbitListener(queues = MQConstants.DEAD_LETTER_QUEUE)  //暂时注释，不让其消费死信队列中的消息
    public void process(@Payload Object sendMessage, Channel channel, Message message) throws Exception {
        logger.info("[{}]处理延迟队列消息队列接收数据，消息体：{}", MQConstants.DEAD_LETTER_QUEUE, JSON.toJSONString(sendMessage));
        try {
            // 参数校验
            Assert.notNull(sendMessage, "sendMessage 消息体不能为NULL");
            // TODO 处理消息
            // 确认消息已经消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("MQ消息处理异常，消息体:{}", message.getMessageProperties().getCorrelationId(), JSON.toJSONString(sendMessage), e);
            try {
                // TODO 保存消息到数据库
                // 确认消息已经消费成功
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception dbe) {
                logger.error("保存异常MQ消息到数据库异常，放到死性队列，消息体：{}", JSON.toJSONString(sendMessage), dbe);
                // 确认消息将消息放到死信队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        }
    }
}
