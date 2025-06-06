package com.r2m.saga.inventorymanager.stock.infrastructure.messaging;

import com.r2m.saga.inventorymanager.shared.domain.Message;
import com.r2m.saga.inventorymanager.stock.application.RegisterItem;
import com.r2m.saga.inventorymanager.stock.domain.event.MarkStockItemAsNotStored;
import com.r2m.saga.inventorymanager.stock.domain.event.RegisterStockItemEvent;
import com.r2m.saga.inventorymanager.storage.domain.event.UpdateStorageEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CompensateRegisterItemConsumer {

  private final RegisterItem registerItem;

  @Autowired
  public CompensateRegisterItemConsumer(RegisterItem registerItem) {
    this.registerItem = registerItem;
  }

  @RabbitListener(queues = {"compensate.item.register"})
  public void compensateRegisterItem(@Payload MarkStockItemAsNotStored event) throws IOException, ClassNotFoundException {
    registerItem.compensate(event);
  }
}
