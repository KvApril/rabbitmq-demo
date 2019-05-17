package com.kv.mqdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kv.mqdemo.rabbitmq.consumer.MQConsumer;
import com.kv.mqdemo.rabbitmq.producer.MQProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqDemoApplicationTests {

    @Autowired
    private MQProducer producer;

    @Autowired
    private MQConsumer consumer;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSendStringMessage() {
        for (int i = 0; i < 10; i++) {
            producer.sendStringMessage("---send---"+i);
        }
    }

    @Test
    public void testSendObjectMessage() {
        for (int i = 1; i < 5; i++) {
            producer.sendObjectMessage(i);
        }
    }

    @Test
    public void testSendIntegerMessage() {
        for (int i = 0; i < 10; i++) {
            producer.sendIntegerMessage(i);
        }
    }

}
