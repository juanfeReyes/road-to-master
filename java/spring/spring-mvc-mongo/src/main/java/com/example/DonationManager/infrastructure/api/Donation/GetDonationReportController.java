package com.example.DonationManager.infrastructure.api.Donation;

import com.example.DonationManager.application.Donation.GetDonationsReport;
import com.example.DonationManager.domain.Donation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("donations")
@Tag(name = "Donation")
public class GetDonationReportController {

  private final GetDonationsReport donationsReport;

  public GetDonationReportController(GetDonationsReport donationsReport) {
    this.donationsReport = donationsReport;
  }

  @Operation(summary = "Download donations report", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('READ')")
  public ResponseEntity<Resource> getDonation() throws IOException {
    var content =  donationsReport.execute();
    var contentUrl = new UrlResource(content.toUri());

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=/report.json")
        .contentLength(contentUrl.contentLength())
        .body(contentUrl);
  }
}
