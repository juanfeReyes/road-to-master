package com.r2m.observabilityapi.application;

import com.r2m.observabilityapi.infrastructure.persistence.StockItem;
import com.r2m.observabilityapi.infrastructure.persistence.repositories.ShelveRepository;
import com.r2m.observabilityapi.infrastructure.persistence.repositories.StockItemRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterStockItem {

  private final ShelveRepository shelveRepository;

  private final StockItemRepository stockItemRepository;


  @Autowired
  public RegisterStockItem(ShelveRepository shelveRepository, StockItemRepository stockItemRepository) {
    this.shelveRepository = shelveRepository;
    this.stockItemRepository = stockItemRepository;
  }

  @Observed(name = "register-stock")
  public StockItem register(String id, StockItem stockItem) throws Exception {
    var shelve = shelveRepository.findById(id);

    if( shelve.isEmpty() ){
      throw new Exception("Shelve does not exist");
    }

    stockItem.setShelve(shelve.get());

    return stockItemRepository.save(stockItem);
  }
}
