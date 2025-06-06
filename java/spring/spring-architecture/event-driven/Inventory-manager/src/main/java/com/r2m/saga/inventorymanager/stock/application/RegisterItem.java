package com.r2m.saga.inventorymanager.stock.application;

import com.r2m.saga.inventorymanager.shared.application.Saga;
import com.r2m.saga.inventorymanager.shared.domain.Message;
import com.r2m.saga.inventorymanager.stock.application.mapper.StockItemEventMapper;
import com.r2m.saga.inventorymanager.stock.domain.StockItem;
import com.r2m.saga.inventorymanager.stock.domain.command.RegisterStockItemCommand;
import com.r2m.saga.inventorymanager.stock.domain.event.MarkStockItemAsNotStored;
import com.r2m.saga.inventorymanager.stock.infrastructure.persistence.StockItemMapper;
import com.r2m.saga.inventorymanager.stock.infrastructure.persistence.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class RegisterItem implements Saga {

  private final StockItemEventMapper stockItemEventMapper;

  private final StockItemMapper stockItemMapper;

  private final StockItemRepository stockItemRepository;

  private final StreamBridge streamBridge;

  @Autowired
  public RegisterItem(StockItemEventMapper stockItemEventMapper, StockItemMapper stockItemMapper, StockItemRepository stockItemRepository, StreamBridge streamBridge) {
    this.stockItemEventMapper = stockItemEventMapper;
    this.stockItemMapper = stockItemMapper;
    this.stockItemRepository = stockItemRepository;
    this.streamBridge = streamBridge;
  }

  @Override
  public Message execute(Message command) {
    var item = StockItem.builder().build();
    var event = stockItemEventMapper.toEvent((RegisterStockItemCommand) command);
    event.apply(item);

    stockItemRepository.save(stockItemMapper.toEntity(item));
    // publish event
    streamBridge.send("update.storage.topic", event);
    return event;
  }

  @Override
  public void compensate(Message message) {
    var event = (MarkStockItemAsNotStored) message;
    var item = stockItemMapper.toAggregate(stockItemRepository.findById(event.itemId()).get()) ;
    event.apply(item);

    stockItemRepository.save(stockItemMapper.toEntity(item));
  }
}
