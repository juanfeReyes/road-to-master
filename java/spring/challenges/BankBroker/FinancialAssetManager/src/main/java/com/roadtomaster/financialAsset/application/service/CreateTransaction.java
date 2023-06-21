package com.roadtomaster.financialAsset.application.service;

import com.roadtomaster.financialAsset.application.adapter.AccountMapper;
import com.roadtomaster.financialAsset.application.adapter.TransactionMapper;
import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import com.roadtomaster.financialAsset.domain.service.WireTransferService;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountRepository;
import com.roadtomaster.financialAsset.infrastructure.persistence.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static com.roadtomaster.financialAsset.infrastructure.config.CacheConfig.TRANSACTIONS_CACHE_NAME;

@Service
public class CreateTransaction {

  private final TransactionRepository transactionRepository;

  private final TransactionMapper transactionMapper;

  private final AccountRepository accountRepository;

  private final AccountMapper accountMapper;

  private final WireTransferService wireTransferService;

  private final CacheManager cacheManager;

  @Autowired
  public CreateTransaction(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountRepository accountRepository, AccountMapper accountMapper, WireTransferService wireTransferService, CacheManager cacheManager) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
    this.wireTransferService = wireTransferService;
    this.cacheManager = cacheManager;
  }

  @Transactional
  public Transaction saveTransaction(UUID originId, UUID destinationId, double amount) {
    var originOptional = accountRepository.findById(originId);
    if (originOptional.isEmpty()) {
      throw new RuntimeException("Origin account does not exists");
    }

    var destinationOptional = accountRepository.findById(destinationId);
    if (destinationOptional.isEmpty()) {
      throw new RuntimeException("Origin account does not exists");
    }

    var origin = accountMapper.toDomain(originOptional.get());
    var destination = accountMapper.toDomain(destinationOptional.get());

    var transaction = wireTransferService.transferMoney(origin, destination, amount);

    accountRepository.save(accountMapper.toTable(origin));
    accountRepository.save(accountMapper.toTable(destination));
    transactionRepository.save(transactionMapper.toTable(transaction));
    cleanCache(origin);

    return transaction;
  }

  private void cleanCache(Account account){
    Objects.requireNonNull(cacheManager.getCache(TRANSACTIONS_CACHE_NAME)).evictIfPresent(account.getOwnerId());
  }
}
