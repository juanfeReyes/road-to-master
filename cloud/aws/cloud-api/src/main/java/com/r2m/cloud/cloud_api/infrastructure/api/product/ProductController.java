package com.r2m.cloud.cloud_api.infrastructure.api.product;

import com.r2m.cloud.cloud_api.application.product.CreateProduct;
import com.r2m.cloud.cloud_api.application.product.SearchProducts;
import com.r2m.cloud.cloud_api.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProduct createProduct;

    private final SearchProducts searchProducts;

    @Autowired
    public ProductController(CreateProduct createProduct, SearchProducts searchProducts) {
        this.createProduct = createProduct;
        this.searchProducts = searchProducts;
    }

    @PostMapping
    public void createProduct(@RequestBody Product product){
        createProduct.execute(product);
    }

    @GetMapping
    public List<Product> getProducts(){
        return searchProducts.execute();
    }
}
