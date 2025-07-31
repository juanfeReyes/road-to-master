package com.r2m.architecture.eventdriven.spring_boot_kafka.application;

import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.producer.ChatProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendChatService {

    private final ChatProducer chatProducer;

    @Autowired
    public SendChatService(ChatProducer chatProducer) {
        this.chatProducer = chatProducer;
    }

    public void execute(String message){
        chatProducer.send(message);
    }
}
