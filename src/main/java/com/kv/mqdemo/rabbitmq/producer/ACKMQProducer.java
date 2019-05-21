package com.kv.mqdemo.rabbitmq.producer;

import com.kv.mqdemo.rabbitmq.MQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class ACKMQProducer {
    private static final Logger log = LoggerFactory.getLogger(ACKMQProducer.class);

    @Resource(name = "ACKRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    //正常的生产消费
    public void sendStringMessage(String message){
        log.info("sender message: {}",message);
        String uuid1 = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(MQConstants.ACK_EXCHANGE, MQConstants.ACK_ROUTING_KEY, message,new CorrelationData(uuid1));
    }

    /**
     * 将消息发送到一个不被消费的队列中
     * 模拟：将ACKConsumer中的Listener注释，不去消费该队列中的消息
     * 结果：消息阻塞在队列中，confirmCallBack触发，打印出原因
     * @param message
     */
    public void testConfirmCallBack(String message){
        log.info("sender message: {}",message);
        String uuid1 = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(MQConstants.ACK_EXCHANGE, MQConstants.ACK_ROUTING_KEY, message,new CorrelationData(uuid1));
    }

    /**
     * 发送到交换器成功，但是没有匹配的队列,会触发 ReturnCallback 回调
     * 模拟：先把项目启动，然后再把队列解绑，再发送消息
     * 结果：发现消息丢失了，没有到任何队列
      * @param message
     */
    public void testReturnCallBack(String message){
        log.info("sender message: {}",message);
        String uuid1 = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(MQConstants.ACK_EXCHANGE, MQConstants.ACK_ROUTING_KEY, message,new CorrelationData(uuid1));
    }
}
