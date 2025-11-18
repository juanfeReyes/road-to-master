package com.r2m.batch_api.infrastructure.api;

import com.r2m.batch_api.application.service.jobManagement.JobSearchService;
import com.r2m.batch_api.model.response.JobExecutionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to manage job lifecycle
 */
@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {
    
    private final JobSearchService jobSearchService;


    @GetMapping("/names")
    public List<String> getJobNames() {
        return jobSearchService.getJobNames();
    }

    @GetMapping("/executions")
    public List<JobExecutionResponse> getJobExecutions() {
        return jobSearchService.getJobExecutions();
    }
}
