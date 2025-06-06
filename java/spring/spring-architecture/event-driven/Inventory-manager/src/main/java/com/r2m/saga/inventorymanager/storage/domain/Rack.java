package com.r2m.saga.inventorymanager.storage.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class Rack {

  private String id;

  private String name;

  private String location;

  private int stockCount;

  public void addStock(int stockCount){
    this.stockCount += stockCount;
  }
}
