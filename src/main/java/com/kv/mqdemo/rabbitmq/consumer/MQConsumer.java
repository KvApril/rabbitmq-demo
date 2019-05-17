package com.kv.mqdemo.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kv.mqdemo.entity.User;
import com.kv.mqdemo.rabbitmq.MQConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MQConsumer {

    @RabbitListener(queues = MQConstants.QUEUE)
    public void recieve(@Payload String message) {
        System.out.println(message);
    }

    @RabbitListener(queues = MQConstants.QUEUE1)
    public void recieve(@Payload  User user) {
        System.out.println(user);
    }

    @RabbitListener(queues = MQConstants.QUEUE2)
    public void recieve(@Payload Integer id,@Headers Map<String,Object>headers) {
        System.out.println(headers);
        System.out.println(id);
    }
}
