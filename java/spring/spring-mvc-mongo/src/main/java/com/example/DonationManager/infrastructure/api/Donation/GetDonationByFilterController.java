package com.example.DonationManager.infrastructure.api.Donation;

import com.example.DonationManager.application.Donation.GetDonationByFilter;
import com.example.DonationManager.domain.Donation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("donations")
@Tag(name = "Donation")
public class GetDonationByFilterController {

  private final GetDonationByFilter getDonationByFilter;

  @Autowired
  public GetDonationByFilterController(GetDonationByFilter getDonationByFilter) {
    this.getDonationByFilter = getDonationByFilter;
  }

  @Operation(summary = "Get Donation by filter", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('READ')")
  public Donation getDonation(@RequestParam String id) {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return getDonationByFilter.execute(id);
  }
}
