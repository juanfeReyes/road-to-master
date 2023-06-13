package com.roadtomaster.user.infrastructure.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.roadtomaster.user.domain.specification.PhoneValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

  @NotEmpty
  private String name;

  @NotEmpty
  private String lastName;

  @Email
  private String email;

  @PhoneValid
  private String phone;

  @NotEmpty
  private String bankName;

}
