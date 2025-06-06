package com.r2m.cloud.cloud_api.infrastructure.api.product;

import com.r2m.cloud.cloud_api.application.product.CreateProductLog;
import com.r2m.cloud.cloud_api.application.product.DownloadProductLog;
import com.r2m.cloud.cloud_api.application.product.GetProductLogs;
import com.r2m.cloud.cloud_api.domain.ProductLog;
import com.r2m.cloud.cloud_api.domain.request.CreateProductLogRequest;
import com.r2m.cloud.cloud_api.domain.request.CreateProductLogTagsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/productlogs")
public class ProductLogController {

    private final CreateProductLog createProductLog;

    private final GetProductLogs getProductLogs;

    private final DownloadProductLog downloadProductLog;

    @Autowired
    public ProductLogController(CreateProductLog createProductLog, GetProductLogs getProductLogs, DownloadProductLog downloadProductLog) {
        this.createProductLog = createProductLog;
        this.getProductLogs = getProductLogs;
        this.downloadProductLog = downloadProductLog;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createProductLog(@ModelAttribute  CreateProductLogRequest request){
        createProductLog.execute(request);
    }

    @PostMapping(path = "/file-upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createProductLogByFileUpload(@ModelAttribute  CreateProductLogRequest request){
        createProductLog.executeByUploadFile(request);
    }

    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createProductLogByMultipart(@ModelAttribute  CreateProductLogRequest request){
        createProductLog.executeByUploadMultipartFile(request);
    }

    @PostMapping( path = "/tag", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String createProductLogTagsOnly(@RequestBody CreateProductLogTagsRequest request){
        return createProductLog.executeOnlyTags(request);
    }

    @GetMapping
    public Map<UUID, ProductLog> getProductLogs(){
        return getProductLogs.execute();
    }

    @GetMapping("/{id}/download")
    public String downloadProductLog(@PathVariable String id) throws URISyntaxException {
        return downloadProductLog.execute(id);
    }
}
