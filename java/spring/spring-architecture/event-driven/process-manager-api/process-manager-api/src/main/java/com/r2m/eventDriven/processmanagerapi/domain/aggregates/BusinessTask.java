package com.r2m.eventDriven.processmanagerapi.domain.aggregates;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Jacksonized
@Data
@Builder
@Embeddable
public class BusinessTask implements Serializable {
  //TODO: Implement the hash equal to use name as id
  private String name;
  private String formId;
}
