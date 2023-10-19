package com.r2m.apicomposer.infrastructure.services.projection;

import java.util.List;

public record ProjectionsResponse(String name, List<Double> metrics) {

}
