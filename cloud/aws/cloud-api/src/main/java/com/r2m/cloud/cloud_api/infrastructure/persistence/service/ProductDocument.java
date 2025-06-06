package com.r2m.cloud.cloud_api.infrastructure.persistence.service;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Data
@DynamoDbBean
public class ProductDocument {

    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private UUID id;

    private String name;

    private int amount;

    private double price;
}
