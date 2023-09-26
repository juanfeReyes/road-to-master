package com.r2m.saga.inventorymanager.stock.application.mapper;

import com.r2m.saga.inventorymanager.stock.domain.command.RegisterStockItemCommand;
import com.r2m.saga.inventorymanager.stock.domain.event.RegisterStockItemEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class StockItemEventMapper {

  public abstract RegisterStockItemEvent toEvent(RegisterStockItemCommand registerStockItemCommand);
}
