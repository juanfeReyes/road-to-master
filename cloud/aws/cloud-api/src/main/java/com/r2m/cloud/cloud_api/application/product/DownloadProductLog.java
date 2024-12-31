package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.ProductLog;
import com.r2m.cloud.cloud_api.infrastructure.cloud.aws.S3Service;
import com.r2m.cloud.cloud_api.infrastructure.persistence.ProductLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Uri;

import java.net.URISyntaxException;
import java.util.UUID;

@Service
public class DownloadProductLog {

    private final ProductLogService productLogService;

    private final S3Service s3Service;

    @Autowired
    public DownloadProductLog(ProductLogService productLogService, S3Service s3Service) {
        this.productLogService = productLogService;
        this.s3Service = s3Service;
    }

    public String execute(String id) throws URISyntaxException {
        ProductLog log = productLogService.getAll().get(UUID.fromString(id));
        S3Uri s3Uri = s3Service.getS3Uri(log.objectKey());

        if(s3Uri.bucket().isEmpty() || s3Uri.key().isEmpty()){
            throw new RuntimeException("Invalid uri stored for product log");
        }

        // call download
        return s3Service.getPresignedObjectUrl(s3Uri.bucket().get(), s3Uri.key().get());
    }
}
