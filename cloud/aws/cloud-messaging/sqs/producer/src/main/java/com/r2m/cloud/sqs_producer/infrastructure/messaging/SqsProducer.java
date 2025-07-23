package com.r2m.cloud.sqs_producer.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

public abstract class SqsProducer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public SqsProducer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
    }

    public abstract void produce(Object body);

    // TODO: how to handle exceptions? or send errors? when to retry?
    protected SendMessageResponse send(String queueUrl, Object body) {
        String serializedBody = serializeBody(body);
        return sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(serializedBody)
                .delaySeconds(10)
                .build());
    }

    protected SendMessageBatchResponse send(String queueUrl, List<Object> bodies) {
        List<SendMessageBatchRequestEntry> messages = bodies.stream()
                .map(this::serializeBody)
                .map((s) -> SendMessageBatchRequestEntry.builder().messageBody(s).build())
                .toList();

        return sqsClient.sendMessageBatch(SendMessageBatchRequest.builder()
                .queueUrl(queueUrl)
                .entries(messages)
                .build());
    }

    private String serializeBody(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
