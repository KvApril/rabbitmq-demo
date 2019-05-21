package com.kv.mqdemo.rabbitmq.consumer;

import com.kv.mqdemo.entity.User;
import com.kv.mqdemo.rabbitmq.MQConstants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MQConsumer {

    @RabbitListener(queues = MQConstants.QUEUE)
    public void recieve(@Payload String message, Channel channel) {
        System.out.println(channel);
        System.out.println(message);
    }

    @RabbitListener(queues = MQConstants.QUEUE1)
    public void recieve(@Payload  User user) {
        System.out.println(user);
    }
}
