package com.r2m.batch_api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JobExecutionResponse implements Serializable {

    private String jobName;

    private String status;

    private Map<String, String> parameters;
}
