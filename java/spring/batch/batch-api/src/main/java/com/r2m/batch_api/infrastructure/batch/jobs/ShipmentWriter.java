package com.r2m.batch_api.infrastructure.batch.jobs;

import com.r2m.batch_api.model.domain.Shipment;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

@Service
public class ShipmentWriter implements ItemWriter<Shipment> {

    @Override
    public void write(Chunk<? extends Shipment> chunk) throws Exception {

    }
}
