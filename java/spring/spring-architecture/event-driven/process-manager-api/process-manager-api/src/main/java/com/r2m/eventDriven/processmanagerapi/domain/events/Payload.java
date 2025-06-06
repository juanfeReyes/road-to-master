package com.r2m.eventDriven.processmanagerapi.domain.events;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Jacksonized
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@class")
public abstract class Payload implements Serializable {

  public abstract Object apply(Object aggregate);

}
