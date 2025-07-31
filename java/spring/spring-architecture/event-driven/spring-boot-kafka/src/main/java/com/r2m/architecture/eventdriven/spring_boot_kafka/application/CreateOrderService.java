package com.r2m.architecture.eventdriven.spring_boot_kafka.application;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.ORDERS_TOPIC;

@Service
public class CreateOrderService {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Autowired
    public CreateOrderService(ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public void execute(String order) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDERS_TOPIC, order);
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
        String value = replyFuture.get(20, TimeUnit.SECONDS).value();
        System.out.println("Sync messaging completed: " + value);
    }
}
