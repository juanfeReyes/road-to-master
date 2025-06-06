package com.r2m.saga.inventorymanager.stock.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Data
@Entity
public class StockItemEntity {

  @Id
  private String id;

  private String description;


  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, String> metadata;

  private String storageId;

  private int stockCount;
}
