package com.r2m.cloud.cloud_api.infrastructure.persistence.service;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.cloud.aws.DynamoService;
import com.r2m.cloud.cloud_api.infrastructure.mappers.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(name = "cloud-api.integration.service", havingValue = "dynamo")
public class DynamoProductService implements IProductService {

    private static final String TABLE_NAME = "products";

    private final DynamoService dynamoService;

    private final ProductMapper productMapper;

    @Autowired
    public DynamoProductService(DynamoService dynamoService, ProductMapper productMapper) {
        this.dynamoService = dynamoService;
        this.productMapper = productMapper;
    }

    @Override
    public void createProduct(Product product) {
        ProductDocument document = productMapper.toDocument(product);
        dynamoService.puItem(TABLE_NAME, document);
        log.info("Successfully created product");

    }

    @Override
    public List<Product> getProducts() {
        return dynamoService
                .scanItems(TABLE_NAME, ProductDocument.class)
                .stream()
                .map(productMapper::toDomain)
                .toList();
    }
}
