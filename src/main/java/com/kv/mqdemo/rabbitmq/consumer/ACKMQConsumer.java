package com.kv.mqdemo.rabbitmq.consumer;

import com.kv.mqdemo.rabbitmq.MQConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ACKMQConsumer{
    private static final Logger log = LoggerFactory.getLogger(ACKMQConsumer.class);

    @RabbitListener(queues = MQConstants.ACK_QUEUE)
    public void recieve(@Payload String mess, Message message, Channel channel) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        //代表投递的标识符，唯一标识了当前信道上的投递。
        long deliveryTag = messageProperties.getDeliveryTag();
        //如果是重复投递的消息，redelivered为true
        Boolean redelivered = messageProperties.getRedelivered();

        log.info("consume message = {},deliveryTag = {}, redelivered = {}",mess,deliveryTag,redelivered);
        try {
            //代表消费者确认收到当前消息，第二个参数标识一次是否ack多条消息
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            //代表消费者拒绝一条或者多条消息，第二个参数标识一次是否拒绝多条消息，第三个参数标识是否把当前消息重新入队
            channel.basicNack(deliveryTag,false,false);

            //代表消费者拒绝[当前]消息，第二个参数标识是否把当前消息重新入队
//            channel.basicReject(deliveryTag,false);
        }
    }
}
