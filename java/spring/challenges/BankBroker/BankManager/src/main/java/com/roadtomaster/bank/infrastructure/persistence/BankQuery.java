package com.roadtomaster.bank.infrastructure.persistence;


import com.querydsl.core.BooleanBuilder;
import lombok.Data;

import java.util.UUID;

@Data
public class BankQuery {

  private UUID id;

  private String name;

  public BooleanBuilder toPredicate() {
    var qBank = QBankTable.bankTable;
    var predicate = new BooleanBuilder();

    if (id != null) {
      predicate.or(qBank.id.eq(id));
    }

    if (name != null) {
      predicate.or(qBank.name.containsIgnoreCase(name));
    }

    return predicate;
  }
}
