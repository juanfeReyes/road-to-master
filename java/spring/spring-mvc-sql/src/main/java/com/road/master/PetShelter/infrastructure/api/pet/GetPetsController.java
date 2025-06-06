package com.road.master.PetShelter.infrastructure.api.pet;

import com.road.master.PetShelter.application.pet.GetPets;
import com.road.master.PetShelter.domain.pet.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("pets")
@Tag(name = "Pets")
public class GetPetsController {

  private final GetPets getPets;

  @Autowired
  public GetPetsController(GetPets getPets) {
    this.getPets = getPets;
  }

  @Operation(summary = "Get all pets", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('READ')")
  public List<Pet> getPets() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return getPets.execute();
  }
}
