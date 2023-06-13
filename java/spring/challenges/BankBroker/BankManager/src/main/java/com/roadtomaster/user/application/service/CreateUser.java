package com.roadtomaster.user.application.service;

import com.roadtomaster.bank.application.adapter.BankMapper;
import com.roadtomaster.bank.infrastructure.persistence.BankRepository;
import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.infrastructure.persistence.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUser {

  private final UserRepository userRepository;

  private final BankRepository bankRepository;

  private final UserMapper userMapper;

  private final BankMapper bankMapper;

  @Autowired
  public CreateUser(UserRepository userRepository, BankRepository bankRepository, UserMapper userMapper, BankMapper bankMapper) {
    this.userRepository = userRepository;
    this.bankRepository = bankRepository;
    this.userMapper = userMapper;
    this.bankMapper = bankMapper;
  }

  @Transactional
  public User saveUser(@Valid User user, String bankName) {
    var bankOptional = bankRepository.findByName(bankName);

    if (bankOptional.isEmpty()) {
      throw new RuntimeException("Bank does not exist");
    }

    if (userRepository.existsByEmail(user.getEmail())) {
      throw new RuntimeException("User with email already exists");
    }

    var bank = bankMapper.toDomain(bankOptional.get());
    var newUser = new User(user.getName(), user.getLastName(), user.getEmail(), user.getPhone());
    var savedUser = userMapper.toDomain(userRepository.save(userMapper.toTable(newUser)));

    bank.addUser(savedUser);
    bankRepository.save(bankMapper.toTable(bank));

    return savedUser;
  }
}
