package com.roadtomaster.financialAsset.application.adapter;

import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "balance", target = "balance")
  @Mapping(source = "ownerId", target = "ownerId")
  Account toDomain(AccountTable accountTable);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "balance", target = "balance")
  @Mapping(source = "ownerId", target = "ownerId")
  AccountTable toTable(Account account);
}
