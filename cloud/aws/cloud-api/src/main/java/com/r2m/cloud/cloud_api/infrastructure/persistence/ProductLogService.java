package com.r2m.cloud.cloud_api.infrastructure.persistence;

import com.r2m.cloud.cloud_api.domain.ProductLog;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductLogService {

    // Temporal storage
    private final Map<UUID, ProductLog> logs;

    public ProductLogService() {
        this.logs = new HashMap<>();
    }

    public void put(UUID key, ProductLog value){
        logs.put(key, value);
    }

    public Map<UUID, ProductLog> getAll(){
        return logs;
    }
}
