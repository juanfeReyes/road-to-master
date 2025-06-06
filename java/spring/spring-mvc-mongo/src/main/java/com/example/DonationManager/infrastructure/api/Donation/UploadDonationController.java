package com.example.DonationManager.infrastructure.api.Donation;

import com.example.DonationManager.application.Donation.LoadResourcesToDonation;
import com.example.DonationManager.domain.Resource;
import com.example.DonationManager.infrastructure.api.Donation.DTO.DonationApiMapper;
import com.example.DonationManager.infrastructure.api.Donation.DTO.ResourceCsv;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("donations")
@Tag(name = "Donation")
public class UploadDonationController {

  private final DonationApiMapper donationApiMapper;

  private final LoadResourcesToDonation loadResourcesToDonation;

  public UploadDonationController(DonationApiMapper donationApiMapper, LoadResourcesToDonation loadResourcesToDonation) {
    this.donationApiMapper = donationApiMapper;
    this.loadResourcesToDonation = loadResourcesToDonation;
  }

  //TODO: Return Donation info plus, total number of processed resources
  @PreAuthorize("hasRole('WRITE')")
  @Operation(summary = "Upload csv of resources", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "/{donationId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadDonation(@RequestParam("csv") MultipartFile file, @PathVariable String donationId) throws IOException, CsvException {
    var resources = parseResources(file);
    loadResourcesToDonation.execute(donationId, resources);
  }

  private List<Resource> parseResources(MultipartFile file) throws IOException {
    Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
    var csvReader = new CSVReader(reader);
    var resourceCsv = new CsvToBeanBuilder<ResourceCsv>(csvReader).withType(ResourceCsv.class).build().parse();
    return resourceCsv.stream().map(donationApiMapper::resourceToDomain).collect(Collectors.toList());
  }
}
