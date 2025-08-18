package com.r2m.cloud.sqs_producer.application.alarm;

import com.r2m.cloud.sqs_producer.infrastructure.persistence.AlarmsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAlarmsService {

    private final AlarmsStore alarmsStore;

    @Autowired
    public GetAlarmsService(AlarmsStore alarmsStore) {
        this.alarmsStore = alarmsStore;
    }

    public List<String> execute(){
        return alarmsStore.getAlarms();
    }
}
