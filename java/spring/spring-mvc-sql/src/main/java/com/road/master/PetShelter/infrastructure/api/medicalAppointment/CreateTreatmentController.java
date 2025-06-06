package com.road.master.PetShelter.infrastructure.api.medicalAppointment;

import com.road.master.PetShelter.application.medicalAppointment.CreateTreatment;
import com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto.TreatmentRequest;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("treatment")
@Tag(name = "Medical appointment")
@SecurityRequirement(name = "basicAuth")
public class CreateTreatmentController {

  private final CreateTreatment createTreatment;

  @Autowired
  public CreateTreatmentController(CreateTreatment createTreatment) {
    this.createTreatment = createTreatment;
  }

  @Operation(summary = "Create treatment", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('WRITE')")
  public List<String> createTreatment(@Valid @RequestBody TreatmentRequest treatmentRequest) {
    var treatments = TreatmentRequest.treatmentItemsToDomain(treatmentRequest.getTreatments(), null);
    var createdTreatments = createTreatment.execute(treatmentRequest.getMedicalAppointmentId(), treatments);
    return createdTreatments.stream().map(treatment -> treatment.getId()).collect(Collectors.toList());
  }
}
