package com.roadtomaster.financialAsset.application.service;

import com.roadtomaster.financialAsset.application.adapter.AccountMapper;
import com.roadtomaster.financialAsset.application.adapter.TransactionMapper;
import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import com.roadtomaster.financialAsset.domain.service.WireTransferService;
import com.roadtomaster.financialAsset.persistence.account.AccountRepository;
import com.roadtomaster.financialAsset.persistence.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTransaction {

  private final TransactionRepository transactionRepository;

  private final TransactionMapper transactionMapper;

  private final AccountRepository accountRepository;

  private final AccountMapper accountMapper;

  private final WireTransferService wireTransferService;

  @Autowired
  public CreateTransaction(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountRepository accountRepository, AccountMapper accountMapper, WireTransferService wireTransferService) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
    this.wireTransferService = wireTransferService;
  }

  public Transaction saveTransaction(Account origin, Account destination, double amount){
    if(!accountRepository.existsById(origin.getId())){
      throw new RuntimeException("Origin account does not exists");
    }

    if(!accountRepository.existsById(destination.getId())){
      throw new RuntimeException("Origin account does not exists");
    }

   var transaction = wireTransferService.transferMoney(origin, destination, amount);

    accountRepository.save(accountMapper.toTable(origin));
    accountRepository.save(accountMapper.toTable(destination));
    transactionRepository.save(transactionMapper.toTable(transaction));

    return transaction;
  }
}
