package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public final static String CHAT_TOPIC = "r2m.events.chat";
    public final static String ORDERS_TOPIC = "r2m.events.orders";
    public final static String SHIPMENT_TOPIC = "r2m.events.shipment";

    public NewTopic chatTopic() {
        return TopicBuilder
                .name(CHAT_TOPIC)
                .partitions(1)
                .build();
    }

    /**
     * This is the request topic
     * @return
     */
    public NewTopic orderTopic() {
        return TopicBuilder
                .name(ORDERS_TOPIC)
                .build();
    }

    /**
     * This is the reply topic
     * @return
     */
    public NewTopic shipmentTopic() {
        return TopicBuilder
                .name(SHIPMENT_TOPIC)
                .build();
    }
}
