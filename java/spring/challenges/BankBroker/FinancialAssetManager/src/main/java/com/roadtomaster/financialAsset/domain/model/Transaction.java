package com.roadtomaster.financialAsset.domain.model;

import com.roadtomaster.financialAsset.domain.TransactionState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {

  @NotNull
  private UUID id;

  @Min(1)
  private double amount;

  @NotNull
  private Account origin;

  @NotNull
  private Account destination;

  @NotNull
  private TransactionState transactionState;

  public Transaction(double amount, Account origin, Account destination) {
    this.amount = amount;
    this.origin = origin;
    this.destination = destination;

    this.id = UUID.randomUUID();
    this.transactionState = TransactionState.PENDING;
  }

  public void completeTransaction() {
    this.transactionState = TransactionState.COMPLETED;
  }
}
