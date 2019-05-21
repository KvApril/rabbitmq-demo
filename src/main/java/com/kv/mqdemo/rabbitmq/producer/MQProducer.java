package com.kv.mqdemo.rabbitmq.producer;

import com.kv.mqdemo.entity.User;
import com.kv.mqdemo.rabbitmq.MQConstants;
import com.kv.mqdemo.service.UserService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    UserService userService;

    /**
     * 发送字符串
     * @param message
     */
    public void sendStringMessage(String message){
        System.out.println("Sender object: " + message);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(MQConstants.EXCHANGE, MQConstants.ROUTING_KEY, message,correlationId);
    }

    /**
     * 发送user对象，User类需要序列化
     * @param id
     */
    public void sendObjectMessage(Integer id) {
        System.out.println("Sender object: " + id);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        User user = userService.queryUserById(id);
        rabbitTemplate.convertAndSend(MQConstants.EXCHANGE, MQConstants.ROUTING_KEY1, user,correlationId);
    }

}
