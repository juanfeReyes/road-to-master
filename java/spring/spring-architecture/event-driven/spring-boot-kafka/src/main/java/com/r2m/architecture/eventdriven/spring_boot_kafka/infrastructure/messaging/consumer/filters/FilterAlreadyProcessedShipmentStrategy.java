package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer.filters;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.util.List;

public class FilterAlreadyProcessedShipmentStrategy implements RecordFilterStrategy {
    @Override
    public List<ConsumerRecord> filterBatch(List list) {
        return RecordFilterStrategy.super.filterBatch(list);
    }

    @Override
    public boolean filter(ConsumerRecord consumerRecord) {

        // Check if shipment is already process and return true to discard
        return false;
    }

    @Override
    public boolean ignoreEmptyBatch() {
        return true;
    }
}
