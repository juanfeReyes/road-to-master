package com.roadtomaster.user.application.service;

import com.roadtomaster.financialAsset.application.adapter.AccountMapper;
import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountRepository;
import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AddAccountToUser {

  private final AccountRepository accountRepository;

  private final UserRepository userRepository;

  private final AccountMapper accountMapper;

  private final UserMapper userMapper;

  @Autowired
  public AddAccountToUser(AccountRepository accountRepository, UserRepository userRepository, AccountMapper accountMapper, UserMapper userMapper) {
    this.accountRepository = accountRepository;
    this.userRepository = userRepository;
    this.accountMapper = accountMapper;
    this.userMapper = userMapper;
  }

  @Transactional
  public Account createAccount(String userId) {
    var userOptional = userRepository.findById(UUID.fromString(userId));
    if (userOptional.isEmpty()) {
      throw new RuntimeException("User does not exist");
    }

    var account = new Account(userId);
    var user = userMapper.toDomain(userOptional.get());
    user.getRegister().addAccount(account);

    var savedAccount = accountRepository.save(accountMapper.toTable(account));
    userRepository.save(userMapper.toTable(user));

    return accountMapper.toDomain(savedAccount);
  }
}
