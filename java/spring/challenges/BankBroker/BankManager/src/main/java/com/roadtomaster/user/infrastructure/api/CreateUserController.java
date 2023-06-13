package com.roadtomaster.user.infrastructure.api;

import com.roadtomaster.user.application.adapter.UserMapper;
import com.roadtomaster.user.application.service.CreateUser;
import com.roadtomaster.user.domain.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Manage users")
public class CreateUserController {

  private final CreateUser createUserService;

  private final UserMapper userMapper;

  @Autowired
  public CreateUserController(CreateUser createUserController, UserMapper userMapper) {
    this.createUserService = createUserController;
    this.userMapper = userMapper;
  }

  @PostMapping("")
  public User createUser(@RequestBody @Valid UserRequest user) {
    return createUserService.saveUser(userMapper.toDomain(user), user.getBankName());
  }
}
