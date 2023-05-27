package com.roadtomaster.bank.persistence;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.UUID;

public class BankQuery {

  private UUID id;

  private String name;

  public BooleanExpression toPredicate(){
    var qBank = QBankTable.bankTable;
    var predicate = Expressions.asBoolean(true).isTrue();
    
    if(id != null){
      predicate.or(qBank.id.eq(id));
    }

    if(name != null){
      predicate.or(qBank.name.like(name));
    }

    return predicate;
  }
}
