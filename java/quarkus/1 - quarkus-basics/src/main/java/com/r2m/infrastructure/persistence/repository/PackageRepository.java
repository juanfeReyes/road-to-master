package com.r2m.infrastructure.persistence.repository;

import com.r2m.infrastructure.persistence.entity.PackageEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PackageRepository implements PanacheRepository<PackageEntity> {
}
