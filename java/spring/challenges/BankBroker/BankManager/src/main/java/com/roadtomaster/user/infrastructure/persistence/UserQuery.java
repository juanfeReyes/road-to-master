package com.roadtomaster.user.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import lombok.Data;

import java.util.UUID;

@Data
public class UserQuery {

  private UUID id;

  private String name;

  private String lastName;

  private String email;

  private String phone;

  public BooleanBuilder toPredicate() {
    var qUser = QUserTable.userTable;
    var builder = new BooleanBuilder();

    if (id != null) {
      builder.or(qUser.id.eq(id));
    }
    if (name != null) {
      builder.or(qUser.name.containsIgnoreCase(name));
    }
    if (lastName != null) {
      builder.or(qUser.lastName.containsIgnoreCase(lastName));
    }
    if (email != null) {
      builder.or(qUser.email.containsIgnoreCase(email));
    }
    if (phone != null) {
      builder.or(qUser.phone.like(phone));
    }

    return builder;
  }
}
