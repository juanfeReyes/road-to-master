package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.ProductLog;
import com.r2m.cloud.cloud_api.infrastructure.persistence.ProductLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class GetProductLogs {

    private final ProductLogService productLogService;

    @Autowired
    public GetProductLogs(ProductLogService productLogService) {
        this.productLogService = productLogService;
    }

    public Map<UUID, ProductLog> execute(){
        return productLogService.getAll();
    }
}
