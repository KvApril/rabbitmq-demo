package com.kv.mqdemo.rabbitmq.config;

import com.kv.mqdemo.rabbitmq.MQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BindingConfig {

    //-------测试普通队列生产消费情况------------------
    @Bean("normalExchange")
    public Exchange normalExchange(RabbitAdmin admin){
        DirectExchange exchange = new DirectExchange(MQConstants.EXCHANGE,true,false);
        admin.declareExchange(exchange);//声明交换机

        //设置队列
        Queue queue = new Queue(MQConstants.QUEUE);
        Queue queue1 = new Queue(MQConstants.QUEUE1);
        admin.declareQueue(queue);//声明队列
        admin.declareQueue(queue1);

        //设置绑定
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(MQConstants.ROUTING_KEY);
        Binding binding1 = BindingBuilder.bind(queue1).to(exchange).with(MQConstants.ROUTING_KEY1);
        admin.declareBinding(binding);
        admin.declareBinding(binding1);
        return exchange;
    }


    //-------测试confirmCallBack和returnCallBack------------------
    @Bean("ackExchange")
    public Exchange ackExchange(RabbitAdmin admin){
        DirectExchange exchange = new DirectExchange(MQConstants.ACK_EXCHANGE,true,false);
        admin.declareExchange(exchange);//声明交换机

        //设置队列
        Queue queue = new Queue(MQConstants.ACK_QUEUE);
        admin.declareQueue(queue);//声明队列

        //设置绑定
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(MQConstants.ACK_ROUTING_KEY);
        admin.declareBinding(binding);
        return exchange;
    }




    @Bean("deadLetterExchange") //DLX-死信邮箱/死信交换机 当队列出现死信时，通过该交换机将死信重新发送到死信队列中
    public Exchange deadLetterExchange(RabbitAdmin admin) {
        DirectExchange directExchange = new DirectExchange(MQConstants.DEAD_LETTER_EXCHANGE,true,false);
        admin.declareExchange(directExchange);

        Queue queue = new Queue(MQConstants.DEAD_LETTER_QUEUE,true);
        admin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(MQConstants.DEAD_LETTER_QUEUE_ROUTING);
        admin.declareBinding(binding);
        return directExchange;
    }

    @Bean("businessExchange") //当BUSINESS_QUEUE队列出现死信时，消息将通过死信交换机+路由键被发送到死信队列(DEAD_LETTER_QUEUE)中
    public Exchange businessExchange(RabbitAdmin admin) {
        DirectExchange directExchange = new DirectExchange(MQConstants.BUSINESS_EXCHANGE,true,false);
        admin.declareExchange(directExchange);

        Queue queue = QueueBuilder.durable(MQConstants.BUSINESS_QUEUE)
                .withArgument("x-dead-letter-exchange", MQConstants.DEAD_LETTER_EXCHANGE) //设置死信交换机
                .withArgument("x-dead-letter-routing-key", MQConstants.DEAD_LETTER_QUEUE_ROUTING) //设置死信路由键
                .build();
        admin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(MQConstants.BUSINESS_QUEUE_ROUTING);
        admin.declareBinding(binding);
        return directExchange;
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