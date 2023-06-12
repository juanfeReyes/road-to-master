package com.roadtomaster.financialAsset.application.service;

import com.roadtomaster.financialAsset.application.adapter.TransactionMapper;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import com.roadtomaster.financialAsset.infrastructure.persistence.transaction.TransactionQuery;
import com.roadtomaster.financialAsset.infrastructure.persistence.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class GetTransactions {

  private final TransactionRepository transactionRepository;

  private final TransactionMapper transactionMapper;

  @Autowired
  public GetTransactions(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
  }

  public List<Transaction> getByUser(String userId){
    var transactions = transactionRepository.findAll(new TransactionQuery().queryByUser(userId));
    return StreamSupport.stream(transactions.spliterator(), false).
            map(transactionMapper::toDomain)
            .toList();
  }
}
