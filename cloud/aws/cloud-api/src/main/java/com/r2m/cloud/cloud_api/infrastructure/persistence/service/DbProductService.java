package com.r2m.cloud.cloud_api.infrastructure.persistence.service;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.mappers.ProductMapper;
import com.r2m.cloud.cloud_api.infrastructure.persistence.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "cloud-api.integration.service", havingValue = "db")
public class DbProductService implements IProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public DbProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public void createProduct(Product product) {
        ProductEntity productEntity = productMapper.toEntity(product);

        productRepository.save(productEntity);
    }

    @Override
    @Cacheable(value = "products")
    public List<Product> getProducts() {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream().map(productMapper::toDomain).toList();
    }
}
