package com.r2m.saga.inventorymanager.storage.domain.command;

import com.r2m.saga.inventorymanager.shared.domain.Message;
import lombok.Value;

public record CreateStorageCommand(String name, String location) implements Message {

}
