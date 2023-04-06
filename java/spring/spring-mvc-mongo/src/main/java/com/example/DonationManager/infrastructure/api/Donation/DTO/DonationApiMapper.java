package com.example.DonationManager.infrastructure.api.Donation.DTO;

import com.example.DonationManager.domain.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface DonationApiMapper {

  @Mapping(source = "description", target = "description")
  @Mapping(source = "amount", target = "amount")
  @Mapping(source = "unit", target = "unit")
  Resource resourceToDomain(ResourceCsv csv);
}
