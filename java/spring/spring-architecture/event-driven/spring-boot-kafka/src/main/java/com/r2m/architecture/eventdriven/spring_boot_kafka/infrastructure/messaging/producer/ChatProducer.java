package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.CHAT_TOPIC;

@Slf4j
@Service
public class ChatProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ChatProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, String>> send(String msg) {
         return kafkaTemplate.send(CHAT_TOPIC, msg);
    }
}
