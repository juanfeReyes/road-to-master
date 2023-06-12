package com.roadtomaster.financialAsset.persistence.account;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<AccountTable, UUID>, QuerydslPredicateExecutor<AccountTable> {
}
