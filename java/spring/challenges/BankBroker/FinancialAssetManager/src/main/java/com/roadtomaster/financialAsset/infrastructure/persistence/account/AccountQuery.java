package com.roadtomaster.financialAsset.infrastructure.persistence.account;


import com.querydsl.core.BooleanBuilder;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountQuery {

  private UUID id;

  private Double balance;

  private String ownerId;

  public BooleanBuilder toPredicate() {
    var qAccount = QAccountTable.accountTable;
    var predicate = new BooleanBuilder();

    if (id != null) {
      predicate.or(qAccount.id.eq(id));
    }
    if (ownerId != null) {
      predicate.or(qAccount.ownerId.eq(ownerId));
    }
    if (balance != null) {
      predicate.or(qAccount.balance.lt(balance));
    }

    return predicate;
  }
}
