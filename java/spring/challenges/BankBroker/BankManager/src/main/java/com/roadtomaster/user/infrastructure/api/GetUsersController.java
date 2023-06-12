package com.roadtomaster.user.infrastructure.api;

import com.roadtomaster.user.application.service.GetUsers;
import com.roadtomaster.user.domain.model.User;
import com.roadtomaster.user.infrastructure.persistence.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class GetUsersController {

  private final GetUsers getUsersService;

  @Autowired
  public GetUsersController(GetUsers getUsersService) {
    this.getUsersService = getUsersService;
  }

  @GetMapping("")
  public List<User> searchUsers(UserQuery userQuery){
    return getUsersService.searchUsers(userQuery);
  }
}
