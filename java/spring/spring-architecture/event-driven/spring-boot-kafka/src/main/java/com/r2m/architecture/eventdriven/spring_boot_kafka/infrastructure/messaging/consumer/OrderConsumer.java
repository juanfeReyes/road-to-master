package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.ORDERS_TOPIC;

@Slf4j
@Component
public class OrderConsumer {

    @SendTo
    @KafkaListener(id = "OrderConsumer", topics = ORDERS_TOPIC, containerFactory = "stringListenerContainer")
    public String listen(@Payload String content, @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.info("Order consumer processing [{}] - message: {}", groupId, content);
        return "Order processed for order: " + content;
    }
}
