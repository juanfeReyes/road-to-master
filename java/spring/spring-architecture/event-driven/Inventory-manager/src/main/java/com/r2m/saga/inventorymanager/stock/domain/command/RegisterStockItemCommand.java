package com.r2m.saga.inventorymanager.stock.domain.command;

import com.r2m.saga.inventorymanager.shared.domain.Command;
import lombok.Value;

import java.util.Map;

public record RegisterStockItemCommand(
        String description,
        Map<String, String> metadata,
        String storageId,
        int stockCount
) implements Command {
}
