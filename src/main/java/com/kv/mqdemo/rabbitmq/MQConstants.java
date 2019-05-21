package com.kv.mqdemo.rabbitmq;

public class MQConstants {
    public final static String EXCHANGE = "my-normal-exchange";
    //这两个queue,用于MQProducer和MQConsumer中,用于测试普通队列的生产消费情况
    public final static String QUEUE = "my-normal-queue-000";
    public final static String QUEUE1 = "my-normal-queue-001";
    //routing-key
    public final static String ROUTING_KEY = "my-normal-routing-key-000";
    public final static String ROUTING_KEY1 = "my-normal-routing-key-001";

    //测试confirmCallBack和returnCallBack
    public final static String ACK_EXCHANGE = "my-ack-exchange";
    public final static String ACK_QUEUE = "my-ack-queue-000";
    public final static String ACK_ROUTING_KEY = "my-ack-routing-key-000";


    //测试死信队列
    public final static String DEAD_LETTER_EXCHANGE = "my-dead-letter-exchange";
    public final static String DEAD_LETTER_QUEUE = "my-dead-letter-queue";
    public final static String DEAD_LETTER_QUEUE_ROUTING = "my-dead-letter-queue-routing";
    public final static String BUSINESS_EXCHANGE = "my-business-exchange";
    public final static String BUSINESS_QUEUE = "my-business-queue";
    public final static String BUSINESS_QUEUE_ROUTING = "my-business-queue-routing";



}
