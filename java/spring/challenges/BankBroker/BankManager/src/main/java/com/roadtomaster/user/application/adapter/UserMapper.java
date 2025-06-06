package com.roadtomaster.user.application.adapter;

import com.roadtomaster.financialAsset.application.adapter.AccountMapper;
import com.roadtomaster.financialAsset.domain.model.Register;
import com.roadtomaster.financialAsset.infrastructure.persistence.RegisterEmbedded;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.infrastructure.api.UserRequest;
import com.roadtomaster.user.infrastructure.persistence.UserTable;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD,
        uses = {AccountMapper.class})
public abstract class UserMapper {

  @Autowired
  protected AccountMapper accountMapper;

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phone", target = "phone")
  @Mapping(target = "register", qualifiedByName = "mapRegister") //TODO: map register
  public abstract User toDomain(UserTable userTable);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phone", target = "phone")
  public abstract User toDomain(UserRequest userRequest);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phone", target = "phone")
  @Mapping(target = "register", qualifiedByName = "mapRegisterEmbedded") //TODO: map register
  public abstract UserTable toTable(User user);

  public abstract Set<User> toDomainList(Set<UserTable> users);

  public abstract Set<UserTable> toTableList(Set<User> users);

  @Named("mapRegister")
  public Register mapRegister(RegisterEmbedded registerEmbedded) {
    return Register.builder()
            .accounts(accountMapper.toDomainList(registerEmbedded.getAccounts()))
            .build();
  }

  @Named("mapRegisterEmbedded")
  public RegisterEmbedded mapRegisterEmbedded(Register register) {
    return RegisterEmbedded.builder()
            .accounts(accountMapper.toTableList(register.getAccounts()))
            .build();
  }
}
