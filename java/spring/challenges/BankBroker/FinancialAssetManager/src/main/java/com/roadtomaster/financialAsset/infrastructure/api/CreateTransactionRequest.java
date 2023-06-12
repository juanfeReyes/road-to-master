package com.roadtomaster.financialAsset.infrastructure.api;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateTransactionRequest {
  private UUID originId;
  private UUID destinationId;
  private double amount;
}
