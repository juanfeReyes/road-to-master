package com.r2m.cloud.cloud_api.infrastructure.mappers;

import com.r2m.cloud.cloud_api.domain.Product;
import com.r2m.cloud.cloud_api.infrastructure.persistence.product.ProductDocument;
import com.r2m.cloud.cloud_api.infrastructure.persistence.product.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public abstract class ProductMapper {

    public abstract ProductEntity toEntity(Product product);
    public abstract Product toDomain(ProductEntity product);
    public abstract ProductDocument toDocument(Product product);
    public abstract Product toDomain(ProductDocument product);
}
