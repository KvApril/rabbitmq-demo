package com.kv.mqdemo.rabbitmq.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConfirmCallBackLister implements RabbitTemplate.ConfirmCallback {
    private static final Logger log = LoggerFactory.getLogger(ConfirmCallBackLister.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        log.info("ConfirmCallBackLister , correlationData = {} , ack = {} , cause = {} ", correlationData, b, s);
        // 如果发送到交换器都没有成功（比如说删除了交换器），ack 返回值为 false
        // 如果发送到交换器成功，但是没有匹配的队列（比如说取消了绑定），ack 返回值为还是 true （这是一个坑，需要注意）
        if (b) {
            log.info("消息id为: "+correlationData+"的消息，已经被ack成功");
        } else {
            log.info("消息id为: "+correlationData+"的消息，消息nack，失败原因是："+s);
        }
    }
}
