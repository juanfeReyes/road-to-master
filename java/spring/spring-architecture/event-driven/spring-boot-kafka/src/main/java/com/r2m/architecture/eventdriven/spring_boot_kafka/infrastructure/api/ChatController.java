package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.api;

import com.r2m.architecture.eventdriven.spring_boot_kafka.application.SendChatService;
import com.r2m.architecture.eventdriven.spring_boot_kafka.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {


    private final SendChatService sendChatService;

    @Autowired
    public ChatController(SendChatService sendChatService) {
        this.sendChatService = sendChatService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@RequestBody String message){
        sendChatService.execute(message);
    }
}
