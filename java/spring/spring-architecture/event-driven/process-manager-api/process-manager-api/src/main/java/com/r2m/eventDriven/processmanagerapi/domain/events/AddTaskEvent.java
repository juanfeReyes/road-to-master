package com.r2m.eventDriven.processmanagerapi.domain.events;

import com.r2m.eventDriven.processmanagerapi.domain.aggregates.BusinessTask;
import com.r2m.eventDriven.processmanagerapi.domain.aggregates.ProcessAggregate;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
public class AddTaskEvent extends Payload {

  private String processId;

  private BusinessTask task;

  @Override
  public ProcessAggregate apply(Object aggregate) {
    var process = (ProcessAggregate) aggregate;
    process.addTask(task);
    return process;
  }
}
