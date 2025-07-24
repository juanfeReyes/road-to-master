package com.r2m.cloud.sqs_producer.infrastructure.messaging.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2m.cloud.sqs_producer.application.shipment.InspectShipmentService;
import com.r2m.cloud.sqs_producer.domain.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class ShipmentConsumer extends SqsConsumer {

    private final ObjectMapper objectMapper;

    private final InspectShipmentService inspectShipmentService;

    @Autowired
    public ShipmentConsumer(
            @Value("${sqs.queue.shipment.url}") String queueUrl,
            ObjectMapper objectMapper, InspectShipmentService inspectShipmentService
    ) {
        super(queueUrl);
        this.objectMapper = objectMapper;
        this.inspectShipmentService = inspectShipmentService;
    }

    @Override
    public void process(Message message) throws JsonProcessingException {
        Shipment shipment = objectMapper.readValue(message.body(), Shipment.class);
        inspectShipmentService.execute(shipment);
    }
}
