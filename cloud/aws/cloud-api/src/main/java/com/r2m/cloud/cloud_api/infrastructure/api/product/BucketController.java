package com.r2m.cloud.cloud_api.infrastructure.api.product;

import com.r2m.cloud.cloud_api.infrastructure.cloud.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.List;

@RestController
@RequestMapping("/buckets")
public class BucketController {

    private final S3Service s3Service;

    @Autowired
    public BucketController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping
    public List<String> getBuckets() {
        return s3Service.getBuckets().stream().map(Bucket::name).toList();
    }

    public void uploadFile() {

    }
}
