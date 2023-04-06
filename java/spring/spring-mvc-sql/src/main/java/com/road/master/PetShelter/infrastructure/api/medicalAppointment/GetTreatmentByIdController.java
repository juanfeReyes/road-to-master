package com.road.master.PetShelter.infrastructure.api.medicalAppointment;

import com.road.master.PetShelter.application.medicalAppointment.GetTreatmentById;
import com.road.master.PetShelter.domain.medicalAppointment.Treatment;
import com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto.TreatmentFilters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("treatment")
@Tag(name = "Medical appointment")
@SecurityRequirement(name = "basicAuth")
public class GetTreatmentByIdController {

  private final GetTreatmentById getTreatmentById;

  @Autowired
  public GetTreatmentByIdController(GetTreatmentById getTreatmentById) {
    this.getTreatmentById = getTreatmentById;
  }

  @Operation(summary = "Get treatment by filters", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('READ')")
  public Treatment getTreatmentById(@PathVariable String id){
    return getTreatmentById.execute(id);
  }
}
