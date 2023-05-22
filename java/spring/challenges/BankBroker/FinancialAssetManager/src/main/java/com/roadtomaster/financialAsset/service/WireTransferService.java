package com.roadtomaster.financialAsset.service;

import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class WireTransferService {

  public Transaction transferMoney(Account origin, Account destination, double amount){
    if(amount <= 0){
      throw new RuntimeException("Transaction amount cannot be less or equal than zero");
    }

    if(origin == null ){
      throw new RuntimeException("Origin account does not exist");
    }

    if(destination == null ){
      throw new RuntimeException("Destination account does not exist");
    }

    if(!origin.hasAvailableBalance(amount)){
      throw new RuntimeException("Origin account has no available balance");
    }

    origin.withdrawal(amount);
    destination.deposit(amount);
    return new Transaction(amount, origin, destination);
  }
}
