package com.r2m.apicomposer.infrastructure.api;

import com.r2m.apicomposer.infrastructure.services.portfolio.PortfolioService;
import com.r2m.apicomposer.infrastructure.services.projection.ProjectionService;
import com.r2m.apicomposer.infrastructure.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class GetUserInfoController {

  private final PortfolioService portfolioService;

  private final ProjectionService projectionService;

  private final UserService userService;

  @Autowired
  public GetUserInfoController(PortfolioService portfolioService, ProjectionService projectionService, UserService userService) {
    this.portfolioService = portfolioService;
    this.projectionService = projectionService;
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public UserInfoResponse getUserInfo(@PathVariable String userId) throws ExecutionException, InterruptedException {
    var userInfo = new UserInfoResponse();
    var portfolioFuture = portfolioService.getPortfolioByUserId(userId);
    var projectionFuture = projectionService.getProjectionByUserId(userId);
    var userFuture = userService.getUser(userId);

    var combinedResponses = portfolioFuture
            .thenCombine(projectionFuture, (portfolio, projection) -> {
              userInfo.setPortfolioResponse(portfolio);
              userInfo.setProjectionsResponse((projection));
              return userInfo;
            }).thenCombine(userFuture, (combined, user) -> {
              userInfo.setUserResponse(user);
              return userInfo;
            });

    return combinedResponses.get();
  }
}
