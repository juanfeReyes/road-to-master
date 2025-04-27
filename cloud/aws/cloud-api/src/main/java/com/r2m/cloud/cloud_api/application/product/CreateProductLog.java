package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.ProductLog;
import com.r2m.cloud.cloud_api.domain.request.CreateProductLogRequest;
import com.r2m.cloud.cloud_api.domain.request.CreateProductLogTagsRequest;
import com.r2m.cloud.cloud_api.domain.utils.FileConverter;
import com.r2m.cloud.cloud_api.infrastructure.cloud.aws.S3Service;
import com.r2m.cloud.cloud_api.infrastructure.persistence.ProductLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateProductLog {

    public final static String LOG_BUCKET_NAME = "cloud-dev-bucket";
    private final static String LOG_DIR_PREFIX = "logs/";

    private final ProductLogService storage;
    private final S3Service s3Service;

    @Autowired
    public CreateProductLog(ProductLogService storage, S3Service s3Service) {
        this.storage = storage;
        this.s3Service = s3Service;
    }

    public void execute(CreateProductLogRequest request) {
        s3Service.transferFile(
                LOG_BUCKET_NAME,
                LOG_DIR_PREFIX + request.file().getOriginalFilename(),
                FileConverter.convertMultipartFileToFile(request.file()));

        storeProductLog(request);
    }

    public void executeByUploadFile(CreateProductLogRequest request) {
        s3Service.uploadFile(
                LOG_BUCKET_NAME,
                LOG_DIR_PREFIX + request.file().getOriginalFilename(),
                FileConverter.convertMultipartFileToFile(request.file()));

        storeProductLog(request);
    }

    public void executeByUploadMultipartFile(CreateProductLogRequest request) {
        s3Service.uploadMultipartFile(
                LOG_BUCKET_NAME,
                LOG_DIR_PREFIX + request.file().getOriginalFilename(),
                request.file());

        storeProductLog(request);
    }

    public String executeOnlyTags(CreateProductLogTagsRequest request) {
        ProductLog log = ProductLog.builder()
                .id(UUID.randomUUID())
                .createTime(Instant.now())
                .tags(request.content())
                .build();

        storage.put(log.id(), log);
        return log.id().toString();
    }

    private String getLogKey(String fileName) {
        return "s3://" + LOG_BUCKET_NAME + "/" + LOG_DIR_PREFIX + fileName;
    }

    private void storeProductLog(CreateProductLogRequest request) {
        ProductLog log = ProductLog.builder()
                .id(UUID.randomUUID())
                .createTime(Instant.now())
                .objectKey(getLogKey(request.file().getOriginalFilename()))
                .build();

        storage.put(log.id(), log);
    }
}
