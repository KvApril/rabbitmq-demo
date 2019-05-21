### rabbitmq-demo

### 普通的生产消费模式
``` 
MQProducer: 普通生产者
MQConsumer: 普通消费者

这两个类用来测试一般的消息的生产，消费流程
```

### confirmCallBack和returnCallBack
``` 
ACKMQProducer: 
ACKMQConsumer:
用来测试confirmCallBack和returnCallback。
和这两个有关的配置详见：RabbitMQConfig中带"ack"的Bean
```

### 死信队列
``` 
BusinessMQProducer:
BusinessMQConsumer:
用来测试死信队列的效果。 
1. 使用了Nack拒绝消息产生的死信。
2. 注意下死信交换机的使用方式
```

所有交换机都是使用的direct模式