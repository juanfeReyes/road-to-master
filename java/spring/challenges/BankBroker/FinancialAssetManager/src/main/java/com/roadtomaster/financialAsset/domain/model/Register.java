package com.roadtomaster.financialAsset.domain.model;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class Register {

  private Set<Account> accounts;

  public Register(){
    this.accounts = new HashSet<>();
  }

  public void addAccount(Account account){
    if(account.getBalance() != 0.0){
      throw new RuntimeException("Account has balance different to 0");
    }
    accounts.add(account);
  }
  public boolean closeAccount(Account account){
    if(account.isCloseable()) {
      return accounts.remove(account);
    }

    throw new RuntimeException("Account has balance different to 0");
  }
}
