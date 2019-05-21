package com.kv.mqdemo.rabbitmq.config;

import com.kv.mqdemo.rabbitmq.callback.ConfirmCallBackLister;
import com.kv.mqdemo.rabbitmq.callback.ReturnCallBackListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
@EnableRabbit
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(BindingConfig.class);

    @Autowired
    private Environment env;

    @Bean  //设置消息转换器，消息发送需要设置contentType为json
    public MessageConverter messageConverter() {
        ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter();
        messageConverter.addDelegate("application/json", new Jackson2JsonMessageConverter());
        return messageConverter;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    //----------------------| MQProducer和MQConsumer使用的配置 start|--------------------->
    @Bean  //自动确认模式设置(auto-ack)
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory());
        simpleRabbitListenerContainerFactory.setMessageConverter(this.messageConverter());
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(2);
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(10);
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return simpleRabbitListenerContainerFactory;
    }

    @Bean //普通连接
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(env.getProperty("spring.rabbitmq.host"));
        connectionFactory.setUsername(env.getProperty("spring.rabbitmq.username"));
        connectionFactory.setPassword(env.getProperty("spring.rabbitmq.password"));
        return connectionFactory;
    }

    @Bean //不需要手动确认和生产者回调
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMandatory(false);
        return rabbitTemplate;
    }
    //----------------------| MQProducer和MQConsumer使用的配置 end|--------------------->




    //----------------------| 测试手动ack和死信队列使用的配置 start|------------------------>
    @Bean("ackRabbitListenerContainerFactory") //手动确认模式(manual-ack)
    public RabbitListenerContainerFactory ackRabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(ackConnectionFactory());
        simpleRabbitListenerContainerFactory.setMessageConverter(this.messageConverter());
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(2);
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(10);
        simpleRabbitListenerContainerFactory.setDefaultRequeueRejected(false);
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return simpleRabbitListenerContainerFactory;
    }

    @Bean("ackConnectionFactory") //开启手动ack确认连接
    public ConnectionFactory ackConnectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(env.getProperty("spring.rabbitmq.host"));
        connectionFactory.setUsername(env.getProperty("spring.rabbitmq.username"));
        connectionFactory.setPassword(env.getProperty("spring.rabbitmq.password"));
        //允许confirmCallback和returnCallback
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean(name = "ACKRabbitTemplate") //需要手动确认和生产者回调
    public RabbitTemplate ACKRabbitTemplate(){
        RabbitTemplate ackRabbitTemplate = new RabbitTemplate(ackConnectionFactory());
        //设置confirmCallback和returnCallback的实现
        ackRabbitTemplate.setConfirmCallback(new ConfirmCallBackLister());
        ackRabbitTemplate.setReturnCallback(new ReturnCallBackListener());
        ackRabbitTemplate.setMandatory(true);
        return ackRabbitTemplate;
    }
    //----------------------| 测试手动ack和死信队列使用的配置 end|--------------------------->

}