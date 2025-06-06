package com.road.master.PetShelter.infrastructure.api.pet;

import com.road.master.PetShelter.application.pet.CreatePet;
import com.road.master.PetShelter.domain.pet.Pet;
import com.road.master.PetShelter.infrastructure.api.pet.dto.PetDTO;
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
@RequestMapping("pets")
@Tag(name = "Pets")
@SecurityRequirement(name = "basicAuth")
public class CreatePetController {

  private final CreatePet createPet;

  @Autowired
  public CreatePetController(CreatePet createPet) {
    this.createPet = createPet;
  }

  @Operation(summary = "Create pet", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('WRITE')")
  public Pet createPet(@Valid @RequestBody PetDTO pet) {
    return createPet.execute(pet.getId(), pet.getName(), pet.getRace());
  }
}
