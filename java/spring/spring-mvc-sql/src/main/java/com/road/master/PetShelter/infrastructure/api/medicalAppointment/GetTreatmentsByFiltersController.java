package com.road.master.PetShelter.infrastructure.api.medicalAppointment;

import com.road.master.PetShelter.application.medicalAppointment.GetTreatmentByFilters;
import com.road.master.PetShelter.infrastructure.api.medicalAppointment.dto.TreatmentFilters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("treatment")
@Tag(name = "Medical appointment")
@SecurityRequirement(name = "basicAuth")
public class GetTreatmentsByFiltersController {

  private GetTreatmentByFilters getTreatmentByFilters;

  @Autowired
  public GetTreatmentsByFiltersController(GetTreatmentByFilters getTreatmentByFilters){
    this.getTreatmentByFilters = getTreatmentByFilters;
  }

  @Operation(summary = "Get treatment by filters", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('WRITE')")
  public Page<String> getTreatmentByFilter(TreatmentFilters filters, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "4") int limit){
    var pageable = PageRequest.of(offset, limit);
    return getTreatmentByFilters.execute(filters.getDosis(), filters.getUnit(), filters.getDescription(), filters.getMedicalAppointmentId(), pageable);
  }
}
