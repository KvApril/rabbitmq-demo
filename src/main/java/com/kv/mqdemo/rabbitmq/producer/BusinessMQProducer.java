package com.kv.mqdemo.rabbitmq.producer;

import com.kv.mqdemo.entity.User;
import com.kv.mqdemo.rabbitmq.MQConstants;
import com.kv.mqdemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * 什么是死信？官方给出三个说法：
 *  1. 消息被拒绝（basic.reject或basic.nack）并且requeue=false.
 *  2. 消息TTL过期
 *  3. 队列达到最大长度（队列满了，无法再添加数据到mq中）
 *
 * 如何使用死信交换机？
 *  定义业务（普通）队列的时候指定参数
 *  1. x-dead-letter-exchange : 用来设置死信后发送的交换机
 *  2. x-dead-letter-routing-key ：用来设置死信的routingKey
 *
 *
 */

@Component
public class BusinessMQProducer {
    private static final Logger log = LoggerFactory.getLogger(BusinessMQProducer.class);

    @Resource(name = "ACKRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    UserService userService;

    /**
     * 生产者发送消息，测试死信消息是否能够到达死信队列
     * 模拟：生产端正常发送，消费端nack消息，模拟第二类死信
     * 结果：查看DEAD_LETTER_QUEUE队列中的消息
     * @param id
     */
    public void testIfToDeadQueue(Integer id){
        String uuid1 = UUID.randomUUID().toString();
        User user = userService.queryUserById(id);
        rabbitTemplate.convertAndSend(MQConstants.BUSINESS_EXCHANGE, MQConstants.BUSINESS_QUEUE_ROUTING, user,new CorrelationData(uuid1));
    }
}
