package com.roadtomaster.bank.application.adapter;

import com.roadtomaster.bank.domain.model.Bank;
import com.roadtomaster.bank.infrastructure.api.BankRequest;
import com.roadtomaster.bank.infrastructure.persistence.BankTable;
import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.infrastructure.persistence.UserTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class BankMapper {

  @Autowired
  protected UserMapper userMapper;

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(target = "users", qualifiedByName = "mapUsers")
  public abstract Bank toDomain(BankTable table);

  @Mapping(source = "name", target = "name")
  public abstract Bank toDomain(BankRequest table);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(target = "users", qualifiedByName = "mapUsersTable")
  public abstract BankTable toTable(Bank bank);

  public abstract Set<Bank> toDomainList(Set<BankTable> banks);

  @Named("mapUsers")
  public Set<User> mapUsers(Set<UserTable> users) {
    return userMapper.toDomainList(users);
  }

  @Named("mapUsersTable")
  public Set<UserTable> mapUsersTable(Set<User> users) {
    return userMapper.toTableList(users);
  }
}
