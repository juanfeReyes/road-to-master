package com.r2m.cloud.sqs_producer.infrastructure.messaging.consumer;

import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;

public abstract class SqsConsumer {

    private final SqsClient sqsClient;

    private final String queueUrl;

    public SqsConsumer(String queueUrl) {
        this.queueUrl = queueUrl;
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    @Scheduled(fixedDelay = 5000)
    public void execute() {
        ReceiveMessageResponse messageResponse = consume(queueUrl, 5);
        if (messageResponse.hasMessages()) {
            for (Message message : messageResponse.messages()) {
                process(message);
            }
            delete(queueUrl, messageResponse.messages());
        }
    }

    public abstract void process(Message message);

    /**
     * Consumer messages from queue
     *
     * @param queueUrl
     * @param maxMessages
     * @return
     */
    public ReceiveMessageResponse consume(String queueUrl, Integer maxMessages) {

        return sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .build());
    }

    public void delete(String queueUrl, List<Message> messages) {
        for (Message message : messages) {
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build());
        }
    }
}
