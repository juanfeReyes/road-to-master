package com.r2m.eventDriven.processmanagerapi.domain.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
public class FormAggregate {

  @Id
  private String id;

  @Column(columnDefinition = "JSON")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, String> form;
}
