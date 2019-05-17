package com.kv.mqdemo.rabbitmq.config;

import com.kv.mqdemo.rabbitmq.MQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BindingConfig {


    @Bean
    public Exchange exchange(RabbitAdmin admin){
        DirectExchange exchange = new DirectExchange(MQConstants.EXCHANGE,true,false);
        exchange.setAdminsThatShouldDeclare(admin);

        Queue queue = new Queue(MQConstants.QUEUE);
        Queue queue1 = new Queue(MQConstants.QUEUE1);
        Queue queue2 = new Queue(MQConstants.QUEUE2);

        admin.declareExchange(exchange);
        admin.declareQueue(queue);
        admin.declareQueue(queue1);
        admin.declareQueue(queue2);

        admin.declareBinding(
                BindingBuilder.bind(queue).to(exchange).with(MQConstants.ROUTING_KEY)
        );
        admin.declareBinding(
                BindingBuilder.bind(queue1).to(exchange).with(MQConstants.ROUTING_KEY1)
        );
        admin.declareBinding(
                BindingBuilder.bind(queue2).to(exchange).with(MQConstants.ROUTING_KEY2)
        );
        return exchange;
    }
//
//    @Bean
//    public Queue queue() {
//        return new Queue("my-queue");
//    }
//
//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange("my-exchange",true,false);
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(exchange()).with("my-routing-key");
//    }
}