package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.mappers.ProductMapper;
import com.r2m.cloud.cloud_api.infrastructure.persistence.ProductEntity;
import com.r2m.cloud.cloud_api.infrastructure.persistence.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProducts {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Autowired
    public SearchProducts(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<Product> execute(){
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream().map(productMapper::toDomain).toList();
    }
}
