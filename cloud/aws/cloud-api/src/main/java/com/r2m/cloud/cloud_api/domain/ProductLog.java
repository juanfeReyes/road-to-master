package com.r2m.cloud.cloud_api.domain;

import lombok.Builder;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@With
public record ProductLog(UUID id, Instant createTime, String content) {

    @Builder
    public ProductLog{}
}
