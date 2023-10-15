package com.r2m.grpc.stockapi.infrastructure.api;

import com.r2m.grpc.stockapi.infrastructure.service.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockController {

  private final StorageClient storageClient;

  @Autowired
  public StockController(StorageClient storageClient) {
    this.storageClient = storageClient;
  }

  @PostMapping("/{id}")
  public void addStock(@PathVariable String id) throws Exception {
    storageClient.updateStorage(id, "test", "test", 100);
  }
}
