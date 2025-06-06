package com.r2m.saga.inventorymanager.storage.domain.command;

import com.r2m.saga.inventorymanager.shared.domain.Command;
import lombok.Value;

public record UpdateStorageCommand(
        String storageId,
        int stockCount
) implements Command {
}
