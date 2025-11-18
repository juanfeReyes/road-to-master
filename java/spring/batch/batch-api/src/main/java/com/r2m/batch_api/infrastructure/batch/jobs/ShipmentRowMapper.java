package com.r2m.batch_api.infrastructure.batch.jobs;

import com.r2m.batch_api.model.domain.Shipment;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class ShipmentRowMapper implements RowMapper<Shipment> {
    @Override
    public Shipment mapRow(RowSet rowSet) throws Exception {

        return Shipment.builder().build();
    }
}