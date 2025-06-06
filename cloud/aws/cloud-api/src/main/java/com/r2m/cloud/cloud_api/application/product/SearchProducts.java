package com.r2m.cloud.cloud_api.application.product;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.persistence.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProducts {

    private final IProductService productService;

    @Autowired
    public SearchProducts(IProductService productService) {
        this.productService = productService;
    }

    public List<Product> execute(){
        return productService.getProducts();
    }
}
