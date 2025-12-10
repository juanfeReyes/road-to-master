package com.r2m.batch_api.application.service.jobManagement;

import com.r2m.batch_api.application.mapper.JobMapper;
import com.r2m.batch_api.model.response.JobExecutionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobSearchService {

    private final JobExplorer jobExplorer;

    private final JobMapper jobMapper;

    public List<String> getJobNames() {
        return jobExplorer.getJobNames();
    }

    public List<JobExecutionResponse> getJobExecutions(){
        var executions = new ArrayList<JobExecution>();
        for (String name : jobExplorer.getJobNames()) {
            for (JobInstance instance : jobExplorer.getJobInstances(name, 0, Integer.MAX_VALUE)) {
                executions.addAll(jobExplorer.getJobExecutions(instance));
            }
        }
        return executions.stream().map(jobMapper::toResponse).toList();
    }
}
