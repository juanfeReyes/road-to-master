package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer;


import com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment;
import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.notification.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.SHIPMENT_TOPIC;

@Slf4j
//@KafkaListener(id = "shipmentConsumerListener",
//        topics = {SHIPMENT_TOPIC},
////        errorHandler = "validationErrorHandler",
////        filter = "filterAlreadyProcessedShipmentStrategy",
//        containerFactory = "multiKafkaListenerContainerFactory"
//)
@Component
public class ShipmentConsumer {

    @Autowired
    private NotificationService notificationService;

//    @KafkaHandler
//    public void listen(@Payload String content) {
//        log.info("String handler content {}", content);
//    }
//
//    //TODO: filtering already processed messages
//    @KafkaListener(id = "shipmentConsumerListener",
//            topics = {SHIPMENT_TOPIC},
//            containerFactory = "multiKafkaListenerContainerFactory"
//    )
//    public void listen(@Payload @Valid Shipment content){
//        log.info("Shipment handler content {}", content);
//    }
//
//    @KafkaHandler(isDefault = true)
//    public void listen(@Payload Object content, @Header(KafkaHeaders.RECORD_METADATA) ConsumerRecordMetadata metadata) {
//        log.info("Default handler metadata {} with content {}", metadata, content);
//    }


//    @KafkaListener(id = "StringShipmentListener", topics = SHIPMENT_TOPIC)
    public void listen(@Payload String content) {
        notificationService.execute(content, "String Shipment listener");
        log.info("Processing shipment {}", content);
    }

//    @KafkaListener(id = "ObjectShipmentListener", topics = SHIPMENT_TOPIC, containerFactory = "objectListenerContainer")
    public void listen(@Payload Object content) {
        notificationService.execute(content, "Object Shipment listener");
        log.info("Processing shipment {}", content);
    }

//    @KafkaListener(id = "JsonObjectListener", topics = SHIPMENT_TOPIC, containerFactory = "jsonListenerContainer")
    public void listenJson(@Payload Object content) {
        notificationService.execute(content, "json object listener");
        log.info("Processing shipment {}", content);
    }

//    @KafkaListener(id = "JsonShipmentListener", topics = SHIPMENT_TOPIC, containerFactory = "jsonListenerContainer")
    public void listenJson(@Payload Shipment content) {
        notificationService.execute(content, "json Shipment listener");
        log.info("Processing shipment {}", content);
    }


    @KafkaListener(id = "JsonMessageShipmentListener", topics = SHIPMENT_TOPIC, containerFactory = "jsonMessageListenerContainer")
    public void listenJsonMessage(@Payload Shipment content) {
        notificationService.execute(content, "json message Shipment listener");
        log.info("Processing shipment {}", content);
    }


}
