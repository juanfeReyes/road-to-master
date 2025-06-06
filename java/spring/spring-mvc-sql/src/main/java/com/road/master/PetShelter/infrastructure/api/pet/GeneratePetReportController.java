package com.road.master.PetShelter.infrastructure.api.pet;


import com.road.master.PetShelter.application.pet.GeneratePetReport;
import com.road.master.PetShelter.domain.pet.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("pets")
@Tag(name = "Pets")
@SecurityRequirement(name = "basicAuth")
public class GeneratePetReportController {

  private final GeneratePetReport generatePetReport;

  @Autowired
  public GeneratePetReportController(GeneratePetReport generatePetReport) {
    this.generatePetReport = generatePetReport;
  }

  @Operation(summary = "Generate report for pets", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('READ')")
  public List<Pet> generateReport(@RequestParam String petRace,
                                      @RequestParam String doctorExpertise,
                                      @RequestParam String adopterAddress,
                                      @RequestParam String adopterName) {
    return generatePetReport.execute(petRace, doctorExpertise, adopterAddress, adopterName);
  }
}
