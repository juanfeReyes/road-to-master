package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.CHAT_TOPIC;

@Component
public class ChatListener {

    @KafkaListener(id = "chatConsumer", topics = CHAT_TOPIC)
    public void listen(String in){
        System.out.println(in);
    }
}
