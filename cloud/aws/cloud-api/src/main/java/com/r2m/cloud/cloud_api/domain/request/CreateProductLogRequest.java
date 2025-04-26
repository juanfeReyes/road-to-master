package com.r2m.cloud.cloud_api.domain.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateProductLogRequest(MultipartFile file) {
}
