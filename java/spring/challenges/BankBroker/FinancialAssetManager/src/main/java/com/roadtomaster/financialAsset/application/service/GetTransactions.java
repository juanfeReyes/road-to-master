package com.roadtomaster.financialAsset.application.service;

import com.roadtomaster.financialAsset.application.adapter.TransactionMapper;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import com.roadtomaster.financialAsset.infrastructure.persistence.transaction.TransactionQuery;
import com.roadtomaster.financialAsset.infrastructure.persistence.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

import static com.roadtomaster.financialAsset.infrastructure.config.CacheConfig.TRANSACTIONS_CACHE_NAME;

@Service
public class GetTransactions {

  private final TransactionRepository transactionRepository;

  private final TransactionMapper transactionMapper;

  @Autowired
  public GetTransactions(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
  }

  @Cacheable(value = TRANSACTIONS_CACHE_NAME, key = "#userId")
  public List<Transaction> getByUser(String userId) {
    var transactions = transactionRepository.findAll(new TransactionQuery().queryByUser(userId));
    return StreamSupport.stream(transactions.spliterator(), false).
            map(transactionMapper::toDomain)
            .toList();
  }
}
