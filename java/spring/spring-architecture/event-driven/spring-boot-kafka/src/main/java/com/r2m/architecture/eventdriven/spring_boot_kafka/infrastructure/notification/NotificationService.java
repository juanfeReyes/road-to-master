package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void execute(Object object, String sender){
        log.info("Sending notification from {} with content {}", sender, object);
    }
}
