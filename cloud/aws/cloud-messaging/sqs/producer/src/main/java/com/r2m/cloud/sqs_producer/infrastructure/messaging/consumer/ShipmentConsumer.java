package com.r2m.cloud.sqs_producer.infrastructure.messaging.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class ShipmentConsumer extends SqsConsumer {

    public ShipmentConsumer(
            @Value("${sqs.queue.shipment.url}") String queueUrl
    ) {
        super(queueUrl);
    }

    @Override
    public void process(Message message) {

    }
}
