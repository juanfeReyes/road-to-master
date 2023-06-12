package com.roadtomaster.user.application.service;

import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.infrastructure.persistence.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Autowired
  public CreateUser(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public User saveUser(@Valid User user){
    if(userRepository.existsByEmail(user.getEmail())){
      throw new RuntimeException("User with email already exists");
    }

    var savedUser = userRepository.save(userMapper.toTable(user));

    return userMapper.toDomain(savedUser);
  }
}
