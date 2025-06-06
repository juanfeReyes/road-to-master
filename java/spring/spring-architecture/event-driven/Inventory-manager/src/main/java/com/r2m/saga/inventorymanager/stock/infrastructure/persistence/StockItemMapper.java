package com.r2m.saga.inventorymanager.stock.infrastructure.persistence;

import com.r2m.saga.inventorymanager.stock.domain.StockItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class StockItemMapper {

  public abstract StockItemEntity toEntity(StockItem stockItem);
  public abstract StockItem toAggregate(StockItemEntity stockItem);
}
