package com.r2m.saga.inventorymanager.storage.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class RackEntity {

  @Id
  private String id;

  private String name;

  private String location;

  private int stockCount;
}
