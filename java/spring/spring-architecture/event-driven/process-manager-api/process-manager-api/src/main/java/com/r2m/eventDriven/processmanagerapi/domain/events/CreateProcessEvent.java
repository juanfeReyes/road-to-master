package com.r2m.eventDriven.processmanagerapi.domain.events;

import com.r2m.eventDriven.processmanagerapi.domain.aggregates.ProcessAggregate;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@Jacksonized
@Value
public class CreateProcessEvent extends Payload {

  public String id;
  public String name;

  @Override
  public ProcessAggregate apply(Object aggregate) {
    var process = (ProcessAggregate) aggregate;
    process.setId(this.id);
    process.setName(name);
    process.setTasks(new HashMap<>());
    return process;
  }
}
