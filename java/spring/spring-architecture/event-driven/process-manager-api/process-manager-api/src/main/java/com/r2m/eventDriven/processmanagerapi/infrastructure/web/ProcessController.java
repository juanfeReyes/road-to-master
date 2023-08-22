package com.r2m.eventDriven.processmanagerapi.infrastructure.web;

import com.r2m.eventDriven.processmanagerapi.application.process.AddTaskCommandHandler;
import com.r2m.eventDriven.processmanagerapi.application.process.CreateProcessCommandHandler;
import com.r2m.eventDriven.processmanagerapi.application.process.GetProcessQueryHandler;
import com.r2m.eventDriven.processmanagerapi.domain.aggregates.ProcessAggregate;
import com.r2m.eventDriven.processmanagerapi.domain.commands.AddTaskCommand;
import com.r2m.eventDriven.processmanagerapi.domain.commands.CreateProcessCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("process")
public class ProcessController {

  private final CreateProcessCommandHandler createProcessCommandHandler;

  private final AddTaskCommandHandler addTaskCommandHandler;

  private final GetProcessQueryHandler getProcessQueryHandler;

  @Autowired
  public ProcessController(CreateProcessCommandHandler createProcessCommandHandler, AddTaskCommandHandler addTaskCommandHandler, GetProcessQueryHandler getProcessQueryHandler) {
    this.createProcessCommandHandler = createProcessCommandHandler;
    this.addTaskCommandHandler = addTaskCommandHandler;
    this.getProcessQueryHandler = getProcessQueryHandler;
  }

  @PostMapping("")
  public void createProcess(@RequestBody CreateProcessCommand command) {
    createProcessCommandHandler.execute(command);
  }

  @PostMapping("/task")
  public void addTask(@RequestBody AddTaskCommand command) {
    addTaskCommandHandler.execute(command);
  }

  @GetMapping("/{processId}")
  public ProcessAggregate getProcess(@PathVariable String processId){
    return getProcessQueryHandler.execute(processId);
  }
}
