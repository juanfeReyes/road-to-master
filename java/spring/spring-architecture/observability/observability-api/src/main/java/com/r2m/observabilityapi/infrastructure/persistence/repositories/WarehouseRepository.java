package com.r2m.observabilityapi.infrastructure.persistence.repositories;

import com.r2m.observabilityapi.infrastructure.persistence.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
}
