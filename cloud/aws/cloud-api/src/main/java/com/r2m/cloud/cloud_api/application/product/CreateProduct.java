package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.persistence.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateProduct {

    private final IProductService productService;

    @Autowired
    public CreateProduct(IProductService productService) {
        this.productService = productService;
    }

    public void execute(Product product){
        productService.createProduct(product);
    }
}
