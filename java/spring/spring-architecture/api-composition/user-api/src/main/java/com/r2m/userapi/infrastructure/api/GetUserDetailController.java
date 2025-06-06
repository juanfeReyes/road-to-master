package com.r2m.userapi.infrastructure.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class GetUserDetailController {

  private final Map<String, UserResponse> users = Map.of(
          "1", new UserResponse("Juan", "reyes", "956 485 6548"),
          "2", new UserResponse("Maria", "reyes", "984 359 648"),
          "3", new UserResponse("Ana", "Garcia", "985 954 6589")
  );

  @GetMapping("/{userId}")
  public UserResponse getUserDetail(@PathVariable String userId) throws InterruptedException {
    Thread.sleep(1500);
    return users.getOrDefault(userId, null);
  }
}
