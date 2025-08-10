package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer;

import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.CHAT_TOPIC;

@Slf4j
@Component
public class ChatConsumer {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(id = "chatConsumer", topics = CHAT_TOPIC, containerFactory = "batchFactory")
    public void listen(List<String> in){
        notificationService.execute(in, "Chat consumer");
    }

    //TODO: ConsumerRecord vs Message?
}
