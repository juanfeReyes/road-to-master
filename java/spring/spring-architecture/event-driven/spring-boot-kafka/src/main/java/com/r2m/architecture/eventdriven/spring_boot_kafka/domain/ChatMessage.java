package com.r2m.architecture.eventdriven.spring_boot_kafka.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {

    private LocalDateTime time;

    private String content;
}
