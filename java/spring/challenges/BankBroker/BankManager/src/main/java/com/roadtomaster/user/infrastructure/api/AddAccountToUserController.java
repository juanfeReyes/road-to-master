package com.roadtomaster.user.infrastructure.api;

import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.user.application.service.AddAccountToUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Manage users")
public class AddAccountToUserController {

  private final AddAccountToUser addAccountToUserService;

  @Autowired
  public AddAccountToUserController(AddAccountToUser addAccountToUserService) {
    this.addAccountToUserService = addAccountToUserService;
  }

  @PutMapping("/{userId}")
  @Operation(summary = "Add a new account to a user", description = "Add a new account to a given user id")
  public Account addAccount(@PathVariable String userId) {
    return addAccountToUserService.createAccount(userId);
  }
}
