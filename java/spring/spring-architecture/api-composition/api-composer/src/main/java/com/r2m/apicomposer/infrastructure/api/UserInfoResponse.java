package com.r2m.apicomposer.infrastructure.api;

import com.r2m.apicomposer.infrastructure.services.portfolio.PortfolioResponse;
import com.r2m.apicomposer.infrastructure.services.projection.ProjectionsResponse;
import com.r2m.apicomposer.infrastructure.services.user.UserResponse;

import java.util.List;

public class UserInfoResponse {

  private List<PortfolioResponse> portfolioResponse;

  private List<ProjectionsResponse> projectionsResponse;

  private UserResponse userResponse;

  public UserInfoResponse(){

  }

  public UserResponse getUserResponse() {
    return userResponse;
  }

  public void setUserResponse(UserResponse userResponse) {
    this.userResponse = userResponse;
  }

  public List<PortfolioResponse> getPortfolioResponse() {
    return portfolioResponse;
  }

  public void setPortfolioResponse(List<PortfolioResponse> portfolioResponse) {
    this.portfolioResponse = portfolioResponse;
  }

  public List<ProjectionsResponse> getProjectionsResponse() {
    return projectionsResponse;
  }

  public void setProjectionsResponse(List<ProjectionsResponse> projectionsResponse) {
    this.projectionsResponse = projectionsResponse;
  }
}
