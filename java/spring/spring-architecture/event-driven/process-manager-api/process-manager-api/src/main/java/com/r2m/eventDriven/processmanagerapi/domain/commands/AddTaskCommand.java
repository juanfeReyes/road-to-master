package com.r2m.eventDriven.processmanagerapi.domain.commands;

import com.r2m.eventDriven.processmanagerapi.domain.aggregates.BusinessTask;
import lombok.Getter;

@Getter
public class AddTaskCommand {

  private String processId;

  private BusinessTask task;
}
