package com.roadtomaster.financialAsset.application.service;

import com.roadtomaster.financialAsset.application.adapter.AccountMapper;
import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountQuery;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class GetAccounts {


  private final AccountRepository accountRepository;

  private final AccountMapper accountMapper;

  @Autowired
  public GetAccounts(AccountRepository accountRepository, AccountMapper accountMapper) {
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
  }

  public List<Account> searchAccounts(AccountQuery accountQuery){
    var accounts = accountRepository.findAll(accountQuery.toPredicate());

    return StreamSupport.stream(accounts.spliterator(), false)
            .map(accountMapper::toDomain)
            .toList();
  }
}
