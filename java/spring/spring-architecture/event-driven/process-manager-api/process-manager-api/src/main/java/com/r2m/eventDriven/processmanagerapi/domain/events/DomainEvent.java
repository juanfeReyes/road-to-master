package com.r2m.eventDriven.processmanagerapi.domain.events;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserTypeSupport;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Jacksonized
@NoArgsConstructor
public class DomainEvent<T extends Payload> {

  @Id
  private String id;

  private String payloadId;
  private LocalDate createdDate;
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  @Type(value = PayloadType.class)
  private T payload;

  public DomainEvent(T payload, String payloadId) {
    id = UUID.randomUUID().toString();
    createdDate = LocalDate.now();
    this.payload = payload;
    this.payloadId = payloadId;
  }
}

class PayloadType extends UserTypeSupport<Payload>{

  public PayloadType(Class<?> returnedClass, int jdbcTypeCode) {
    super(Payload.class, SqlTypes.JSON);
  }
}
