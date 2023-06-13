package com.roadtomaster.user.application.service;

import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.infrastructure.persistence.UserQuery;
import com.roadtomaster.user.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class GetUsers {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Autowired
  public GetUsers(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public List<User> searchUsers(UserQuery searchFilters) {
    var users = userRepository.findAll(searchFilters.toPredicate());

    return StreamSupport.stream(users.spliterator(), false)
            .map(userMapper::toDomain)
            .toList();
  }
}
