package com.roadtomaster.user.application.adapter;

import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.persistence.UserTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phone", target = "phone")
  @Mapping(target = "register", ignore = true) //TODO: map register
  User toDomain(UserTable userTable);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phone", target = "phone")
  @Mapping(target = "register", ignore = true) //TODO: map register
  UserTable toTable(User user);

  Set<User> toDomainList(Set<UserTable> users);

  Set<UserTable> toTableList(Set<User> users);
}
