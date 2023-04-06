package com.road.master.PetShelter.infrastructure.api.medicalAppointment;

import com.road.master.PetShelter.application.medicalAppointment.CreateMedicalAppointment;
import com.road.master.PetShelter.domain.medicalAppointment.MedicalAppointment;
import com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto.MedicalAppointmentRequest;
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
@RequestMapping("medical-appointment")
@Tag(name = "Medical appointment")
@SecurityRequirement(name = "basicAuth")
public class  CreateMedicalAppointmentController {

  private final CreateMedicalAppointment createMedicalAppointment;

  @Autowired
  public CreateMedicalAppointmentController(CreateMedicalAppointment createMedicalAppointment) {
    this.createMedicalAppointment = createMedicalAppointment;
  }

  @Operation(summary = "Create medical appointment", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('WRITE')")
  public MedicalAppointment createMedicalAppointment(@Valid @RequestBody MedicalAppointmentRequest medicalAppointmentRequest) {
    return createMedicalAppointment.execute(medicalAppointmentRequest);
  }
}
