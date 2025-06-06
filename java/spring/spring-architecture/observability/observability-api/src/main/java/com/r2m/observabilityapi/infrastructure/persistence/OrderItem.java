package com.r2m.observabilityapi.infrastructure.persistence;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class OrderItem {

  @Id
  private String id;

  private OrderType type;

  private LocalDate date;

  @OneToOne
  @JoinColumn(name = "transport_id", foreignKey = @ForeignKey(name = "TRANSPORT_ID_FK"))
  private Transport transport;

  @ManyToOne
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "USER_ID_FK"))
  private User user;
}
