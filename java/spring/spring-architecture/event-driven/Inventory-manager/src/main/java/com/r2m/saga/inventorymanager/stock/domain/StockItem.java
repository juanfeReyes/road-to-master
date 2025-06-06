package com.r2m.saga.inventorymanager.stock.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class StockItem {

  private String id;

  private String description;

  private Map<String, String> metadata;

  private String storageId;

  private int stockCount;
}
