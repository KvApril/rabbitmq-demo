package com.kv.mqdemo.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kv.mqdemo.entity.User;
import com.kv.mqdemo.rabbitmq.MQConstants;
import com.kv.mqdemo.service.UserService;
import com.mklinfo.mq.RabbitProducer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.TopicExchange;
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

    public void sendStringMessage(String message){
        System.out.println("Sender object: " + message);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(MQConstants.EXCHANGE, MQConstants.ROUTING_KEY, message,correlationId);
    }

    public void sendObjectMessage(Integer id) {
        System.out.println("Sender object: " + id);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        User user = userService.queryUserById(id);
        rabbitTemplate.convertAndSend(MQConstants.EXCHANGE, MQConstants.ROUTING_KEY1, user,correlationId);
    }

    public void sendIntegerMessage(Integer id) {
        System.out.println("Sender object: " + id);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(MQConstants.EXCHANGE, MQConstants.ROUTING_KEY2, id,correlationId);
    }
}
