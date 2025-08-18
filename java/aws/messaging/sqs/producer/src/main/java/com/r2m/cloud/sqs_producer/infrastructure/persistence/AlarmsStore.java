package com.r2m.cloud.sqs_producer.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlarmsStore {

    private List<String> alarms = new ArrayList<>();

    public List<String> getAlarms(){
        return alarms;
    }

    public void sendAlarm(String alarmMessage){
        alarms.add(alarmMessage);
    }
}
