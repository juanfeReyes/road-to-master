package com.r2m.cloud.sqs_producer.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShipmentMessageProducer extends SqsProducer{

    @Value("${sqs.queue.shipment.url}")
    private String shipmentQueueUrl;

    public ShipmentMessageProducer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void produce(Object body) {
        send(shipmentQueueUrl, body);
    }
}
