package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.ProductLog;
import com.r2m.cloud.cloud_api.domain.request.CreateProductLogRequest;
import com.r2m.cloud.cloud_api.infrastructure.persistence.ProductLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateProductLog {

    private final ProductLogService storage;

    @Autowired
    public CreateProductLog(ProductLogService storage) {
        this.storage = storage;
    }

    public void execute(CreateProductLogRequest request){
        ProductLog log = ProductLog.builder()
                .id(UUID.randomUUID())
                .createTime(Instant.now())
                .content(request.content())
                .build();

        storage.put(log.id(), log);
    }
}
