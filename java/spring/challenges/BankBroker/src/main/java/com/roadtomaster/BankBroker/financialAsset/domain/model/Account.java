package com.roadtomaster.BankBroker.financialAsset.domain.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Account {

  @NotNull
  private UUID id;

  private double balance;

  public Account(){
    this.id = UUID.randomUUID();
    this.balance = 0.0;
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
