package com.roadtomaster.financialAsset.application.adapter;

import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountTable;
import com.roadtomaster.financialAsset.infrastructure.persistence.transaction.TransactionsTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

  @Autowired
  private static AccountMapper accountMapper;

  @Mapping(source = "id", target = "id")
  @Mapping(source = "amount", target = "amount")
  @Mapping(source = "origin", target = "origin", qualifiedByName = "mapDomainAccount")
  @Mapping(source = "destination", target = "destination", qualifiedByName = "mapDomainAccount")
  @Mapping(source = "transactionState", target = "transactionState")
  public abstract Transaction toDomain(TransactionsTable transactionsTable);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "amount", target = "amount")
  @Mapping(source = "origin", target = "origin", qualifiedByName = "mapTableAccount")
  @Mapping(source = "destination", target = "destination", qualifiedByName = "mapTableAccount")
  @Mapping(source = "transactionState", target = "transactionState")
  public abstract TransactionsTable toTable(Transaction transactionsTable);

  @Named("mapDomainAccount")
  public static Account mapDomainAccount(AccountTable accountTable){
    return accountMapper.toDomain(accountTable);
  }

  @Named("mapTableAccount")
  public static AccountTable mapTableAccount(Account account){
    return accountMapper.toTable(account);
  }
}
