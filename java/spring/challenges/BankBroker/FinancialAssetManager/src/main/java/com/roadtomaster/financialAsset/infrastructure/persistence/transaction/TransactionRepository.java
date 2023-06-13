package com.roadtomaster.financialAsset.infrastructure.persistence.transaction;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionsTable, UUID>, QuerydslPredicateExecutor<TransactionsTable> {
}
