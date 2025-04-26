package com.r2m.cloud.cloud_api.infrastructure.persistence.repositories;

import com.r2m.cloud.cloud_api.infrastructure.persistence.service.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
