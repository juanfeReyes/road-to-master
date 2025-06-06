package com.roadtomaster.user.infrastructure.persistence;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserTable, UUID>, QuerydslPredicateExecutor<UserTable> {

  boolean existsByEmail(String email);
}
