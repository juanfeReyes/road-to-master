package com.roadtomaster.financialAsset.domain.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Account {

  @NotNull
  private UUID id;

  private double balance;

  @NotNull
  private String ownerId;

  public Account(String ownerId){
    this.id = UUID.randomUUID();
    this.balance = 0.0;
    this.ownerId = ownerId;
  }

  public boolean isCloseable(){
    return balance == 0.0;
  }

  public void deposit(double amount){
    this.balance += amount;
  }

  public void withdrawal(double amount){
    this.balance -= amount;
  }

  public boolean hasAvailableBalance(double amount){
    return this.balance - amount >= 0.0;
  }

}
