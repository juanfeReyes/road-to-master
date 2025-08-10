package com.r2m.architecture.eventdriven.spring_boot_kafka.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment implements Serializable {

    @NotNull
    private UUID id;

    @Min(5)
    private String address;

    private Set<String> routes;

    private Map<String, LocalDateTime> arrivalTags;


    public static Shipment parse(String data) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(data, Shipment.class);
    }
}
