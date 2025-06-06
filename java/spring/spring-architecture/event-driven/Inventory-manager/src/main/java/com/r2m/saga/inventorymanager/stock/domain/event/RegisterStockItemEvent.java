package com.r2m.saga.inventorymanager.stock.domain.event;

import com.r2m.saga.inventorymanager.shared.domain.Command;
import com.r2m.saga.inventorymanager.stock.domain.StockItem;
import lombok.Data;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Data
public class RegisterStockItemEvent implements Command {
  private String itemId;
  private String description;
  private Map<String, String> metadata;
  private String storageId;
  private int stockCount;

  public void apply(StockItem item) {
    this.itemId = UUID.randomUUID().toString();
    item.setId(itemId);
    item.setDescription(description);
    item.setMetadata(metadata);
    item.setStorageId(storageId);
    item.setStockCount(stockCount);
  }
}
