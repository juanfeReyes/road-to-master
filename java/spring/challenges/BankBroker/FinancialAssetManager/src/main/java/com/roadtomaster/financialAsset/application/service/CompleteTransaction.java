package com.roadtomaster.financialAsset.application.service;

import com.roadtomaster.financialAsset.application.adapter.TransactionMapper;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import com.roadtomaster.financialAsset.persistence.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompleteTransaction {

  private final TransactionRepository transactionRepository;

  private final TransactionMapper transactionMapper;

  @Autowired
  public CompleteTransaction(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
  }

  public Transaction complete(UUID id){
    var foundTransaction = transactionRepository.findById(id);

    if(foundTransaction.isEmpty()){
      throw new RuntimeException("Transaction does not extis");
    }

    var transaction = transactionMapper.toDomain(foundTransaction.get());
    transaction.completeTransaction();

    transactionRepository.save(transactionMapper.toTable(transaction));
    return transaction;
  }
}
