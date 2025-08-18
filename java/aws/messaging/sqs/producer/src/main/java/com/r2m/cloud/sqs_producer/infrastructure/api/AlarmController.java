package com.r2m.cloud.sqs_producer.infrastructure.api;


import com.r2m.cloud.sqs_producer.application.alarm.GetAlarmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/alarms")
public class AlarmController {

    private final GetAlarmsService getAlarmsService;

    @Autowired
    public AlarmController(GetAlarmsService getAlarmsService) {
        this.getAlarmsService = getAlarmsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAlarms(){
        return ResponseEntity.ok(getAlarmsService.execute());
    }
}
