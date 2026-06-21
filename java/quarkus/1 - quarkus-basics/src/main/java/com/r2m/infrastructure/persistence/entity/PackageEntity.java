package com.r2m.infrastructure.persistence.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PackageEntity extends PanacheEntityBase {

    @Id
    private Long id;
}
