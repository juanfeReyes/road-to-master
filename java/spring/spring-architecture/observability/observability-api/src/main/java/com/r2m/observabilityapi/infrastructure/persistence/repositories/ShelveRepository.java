package com.r2m.observabilityapi.infrastructure.persistence.repositories;

import com.r2m.observabilityapi.infrastructure.persistence.Shelve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelveRepository extends JpaRepository<Shelve, String> {

  @Query("select s from Shelve s where s.warehouse.id = :warehouse_id")
  List<Shelve> findAllByWarehouseId(@Param("warehouse_id") String id);
}
