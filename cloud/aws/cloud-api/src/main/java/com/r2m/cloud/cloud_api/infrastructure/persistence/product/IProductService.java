package com.r2m.cloud.cloud_api.infrastructure.persistence.product;

import com.r2m.cloud.cloud_api.domain.Product;

import java.util.List;

public interface IProductService {

    public void createProduct(Product product);

    public List<Product> getProducts();
}
