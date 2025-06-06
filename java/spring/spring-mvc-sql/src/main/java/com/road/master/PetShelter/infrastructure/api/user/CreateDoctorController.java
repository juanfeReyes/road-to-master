package com.road.master.PetShelter.infrastructure.api.user;

import com.road.master.PetShelter.application.user.CreateDoctor;
import com.road.master.PetShelter.domain.user.Doctor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("doctor")
@Tag(name = "User")
@SecurityRequirement(name = "basicAuth")
public class CreateDoctorController {

  private final CreateDoctor createDoctor;

  @Autowired
  public CreateDoctorController(CreateDoctor createDoctor) {
    this.createDoctor = createDoctor;
  }

  @Operation(summary = "Create Doctor", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('WRITE')")
  public Doctor createDoctor(@Valid @RequestBody Doctor doctor) {
    return createDoctor.execute(doctor);
  }
}
