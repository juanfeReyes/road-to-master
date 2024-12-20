package com.r2m.cloud.cloud_api.infrastructure.api.product;

import com.r2m.cloud.cloud_api.application.product.CreateProductLog;
import com.r2m.cloud.cloud_api.application.product.GetProductLogs;
import com.r2m.cloud.cloud_api.domain.ProductLog;
import com.r2m.cloud.cloud_api.domain.request.CreateProductLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/productlogs")
public class ProductLogController {

    private final CreateProductLog createProductLog;

    private final GetProductLogs getProductLogs;

    @Autowired
    public ProductLogController(CreateProductLog createProductLog, GetProductLogs getProductLogs) {
        this.createProductLog = createProductLog;
        this.getProductLogs = getProductLogs;
    }

    @PostMapping
    public void createProductLog(@RequestBody CreateProductLogRequest request){
        createProductLog.execute(request);
    }

    @GetMapping
    public Map<UUID, ProductLog> getProductLogs(){
        return getProductLogs.execute();
    }
}
