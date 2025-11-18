package com.r2m.batch_api.application.mapper;

import com.r2m.batch_api.model.response.JobExecutionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.batch.core.JobExecution;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "jobName", source = "jobExecution.jobInstance.jobName")
    @Mapping(target = "status", source = "jobExecution.status")
    @Mapping(target = "parameters", source = "jobExecution", qualifiedByName = "mapContext")
    JobExecutionResponse toResponse(JobExecution jobExecution);

    @Named("mapContext")
    default Map<String, String> mapContext(JobExecution jobExecution){
        var params = new HashMap<String, String>();
        jobExecution.getJobParameters().getParameters().entrySet()
                .forEach((e) -> {
                    params.put(e.getKey(), (String) e.getValue().getValue());
                });
        return params;
    }
}
