package com.roadtomaster.user.infrastructure.api;

import com.roadtomaster.user.application.service.CreateUser;
import com.roadtomaster.user.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class CreateUserController {

  private final CreateUser createUserService;

  @Autowired
  public CreateUserController(CreateUser createUserController) {
    this.createUserService = createUserController;
  }

  @PostMapping("")
  public User createUser(@RequestBody @Valid User user){
    return createUserService.saveUser(user);
  }
}
