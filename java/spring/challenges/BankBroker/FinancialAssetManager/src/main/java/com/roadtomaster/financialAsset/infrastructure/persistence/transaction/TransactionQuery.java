package com.roadtomaster.financialAsset.infrastructure.persistence.transaction;

import com.querydsl.core.BooleanBuilder;

public class TransactionQuery {

  public BooleanBuilder queryByUser(String userId) {
    var qTransaction = QTransactionsTable.transactionsTable;
    var predicate = new BooleanBuilder();

    predicate.or(qTransaction.origin.ownerId.eq(userId))
            .or(qTransaction.destination.ownerId.eq(userId));

    return predicate;
  }
}
