package com.example.DonationManager.infrastructure.api.Donation;

import com.example.DonationManager.application.Donation.CreateDonation;
import com.example.DonationManager.domain.Donation;
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
@RequestMapping("donations")
@Tag(name = "Donation")
public class CreateDonationController {

  private final CreateDonation createDonation;

  @Autowired
  public CreateDonationController(CreateDonation createDonation) {
    this.createDonation = createDonation;
  }

  @Operation(summary = "Create donation", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('WRITE')")
  public void createDonation(@Valid @RequestBody Donation donation) {
    createDonation.execute(donation);
  }
}
