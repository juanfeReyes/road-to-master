package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.mappers.ProductMapper;
import com.r2m.cloud.cloud_api.infrastructure.persistence.ProductEntity;
import com.r2m.cloud.cloud_api.infrastructure.persistence.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateProduct {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Autowired
    public CreateProduct(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void execute(Product product){
        ProductEntity productEntity = productMapper.toEntity(product);

        productRepository.save(productEntity);
    }
}
