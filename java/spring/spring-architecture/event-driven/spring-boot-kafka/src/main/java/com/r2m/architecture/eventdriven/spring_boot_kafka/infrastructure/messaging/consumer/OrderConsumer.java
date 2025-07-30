package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.ORDERS_TOPIC;
import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.SHIPMENT_TOPIC;

@Component
public class OrderConsumer {

    @SendTo
    @KafkaListener(id = "shipmentConsumer", topics = ORDERS_TOPIC)
    public String listen(String in){
        System.out.println("Shipment consumer processing: " + in);
        return "Order processed for order: "+in;
    }
}
