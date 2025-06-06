package com.r2m.observabilityapi.infrastructure.persistence.repositories;

import com.r2m.observabilityapi.infrastructure.persistence.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, String> {
}
