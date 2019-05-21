package com.kv.mqdemo;

import com.kv.mqdemo.rabbitmq.producer.ACKMQProducer;
import com.kv.mqdemo.rabbitmq.producer.MQProducer;
import com.kv.mqdemo.rabbitmq.producer.BusinessMQProducer;
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
    private ACKMQProducer ackMQProducer;

    @Autowired
    private BusinessMQProducer normalQueueProducer;

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
    public void testAckSendStringMessage() {
        //返回结果
        //c.k.m.r.callback.ConfirmCallBackLister   : ------ConfirmCallBackLister----------
        //c.k.m.r.callback.ConfirmCallBackLister   : 消息id为: CorrelationData [id=d1008955-3e44-483e-88fb-4401c9cac89b]的消息，已经被ack成功
        for (int i = 0; i < 10; i++) {
            ackMQProducer.sendStringMessage("---send---"+i);
        }
    }


    @Test
    public void testConfirmCallBack() {
        //结果输出
        //127.0.0.1:5672] c.k.m.r.callback.ReturnCallBackListener  : ------ReturnCallBackListener----------
        //127.0.0.1:5672] c.k.m.r.callback.ReturnCallBackListener  : return--message:---send---0,replyCode:312,replyText:NO_ROUTE,exchange:my-exchange,routingKey:my-queue-005
        for (int i = 0; i < 10; i++) {
            ackMQProducer.testConfirmCallBack("---send---"+i);
        }
    }

    @Test
    public void testReturnCallBack() {
        //返回结果
//        c.k.m.r.callback.ReturnCallBackListener  : ------ReturnCallBackListener----------
//        c.k.m.r.callback.ReturnCallBackListener  : return--message:---send---0,replyCode:312,replyText:NO_ROUTE,exchange:my-exchange,routingKey:qqq
        for (int i = 0; i < 10; i++) {
            ackMQProducer.testReturnCallBack("---send---"+i);
        }
    }

    @Test
    public void testIfToDeadQueue() {
 //       返回结果
//        c.k.m.r.consumer.BusinessMQConsumer      : consume message = aa,deliveryTag = 1
//        2019-05-21 15:25:13.299  WARN 46452 --- [ntContainer#1-1] c.k.m.r.consumer.BusinessMQConsumer      : 拒绝一条信息:[aa]，此消息将会由死信交换器进行路由.
//        2019-05-21 15:25:13.301  INFO 46452 --- [ntContainer#1-2] c.k.m.r.consumer.BusinessMQConsumer      : consume message = bb,deliveryTag = 1
//        2019-05-21 15:25:13.302  WARN 46452 --- [ntContainer#1-2] c.k.m.r.consumer.BusinessMQConsumer      : 拒绝一条信息:[bb]，此消息将会由死信交换器进行路由.
        for (int i = 1; i < 5; i++) {
            normalQueueProducer.testIfToDeadQueue(i);
        }
    }

}
