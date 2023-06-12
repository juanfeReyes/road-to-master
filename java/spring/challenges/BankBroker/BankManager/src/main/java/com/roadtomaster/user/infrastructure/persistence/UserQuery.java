package com.roadtomaster.user.infrastructure.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.UUID;

public class UserQuery {

  private UUID id;

  private String name;

  private String lastName;

  private String email;

  private String phone;

  public BooleanExpression toPredicate() {
    var qUser = QUserTable.userTable;
    var predicate = Expressions.asBoolean(true).isTrue();

    if (id != null) {
      predicate.or(qUser.id.eq(id));
    }
    if (id != null) {
      predicate.or(qUser.name.like(name));
    }
    if (id != null) {
      predicate.or(qUser.lastName.like(lastName));
    }
    if (id != null) {
      predicate.or(qUser.email.like(email));
    }
    if (id != null) {

      predicate.or(qUser.phone.like(phone));
    }

    return predicate;
  }
}
