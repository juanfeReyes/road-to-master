package com.roadtomaster.bank.application.adapter;

import com.roadtomaster.bank.domain.model.Bank;
import com.roadtomaster.bank.persistence.BankTable;
import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.persistence.UserTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class BankMapper {

  @Autowired
  private static UserMapper userMapper;

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(target = "users", qualifiedByName = "mapUsers")
  public abstract Bank toDomain(BankTable table);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(target = "users", qualifiedByName = "mapUsersTable")
  public abstract BankTable toTable(Bank bank);

  public abstract Set<Bank> toDomainList(Set<BankTable> banks);

  @Named("mapUsers")
  public static Set<User> mapUsers(Set<UserTable> users){
    return userMapper.toDomainList(users);
  }

  @Named("mapUsersTable")
  public static Set<User> mapUsersTable(Set<UserTable> users){
    return userMapper.toDomainList(users);
  }
}
