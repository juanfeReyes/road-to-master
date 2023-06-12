package com.roadtomaster.bank.persistence;

import com.roadtomaster.user.persistence.UserTable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankRepository extends CrudRepository<BankTable, UUID>, QuerydslPredicateExecutor<BankTable> {

  boolean existsByName(String name);
}
