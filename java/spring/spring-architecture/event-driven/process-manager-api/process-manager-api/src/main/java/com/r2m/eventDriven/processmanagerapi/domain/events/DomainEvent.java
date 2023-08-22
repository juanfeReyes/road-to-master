package com.r2m.eventDriven.processmanagerapi.domain.events;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Jacksonized
@NoArgsConstructor
public class DomainEvent {

  @Id
  private String id;

  private String payloadId;
  private LocalDate createdDate;
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Payload payload;

  public DomainEvent(Payload payload, String payloadId) {
    id = UUID.randomUUID().toString();
    createdDate = LocalDate.now();
    this.payload = payload;
    this.payloadId = payloadId;
  }
}
