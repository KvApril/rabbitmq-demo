package com.kv.mqdemo.rabbitmq.consumer;

import com.kv.mqdemo.entity.User;
import com.kv.mqdemo.rabbitmq.MQConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
public class BusinessMQConsumer {
    private static final Logger log = LoggerFactory.getLogger(BusinessMQConsumer.class);

    @RabbitListener(queues = MQConstants.BUSINESS_QUEUE)
    public void recieve(@Payload User user, Message message, Channel channel) throws IOException {
        log.info("consume message = {},deliveryTag = {}",user.getName(),message.getMessageProperties().getDeliveryTag());
        //为测试死信队列，随机拒绝一个消息，查看是否消息是否被重发到死信队列
        try {
            if (new Random().nextInt(10) < 5) {
                log.warn("拒绝一条信息:[{}]，此消息将会由死信交换器进行路由.", user.getName());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } else {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            log.error("ack error: {}",e.getMessage());
        }
    }

}
