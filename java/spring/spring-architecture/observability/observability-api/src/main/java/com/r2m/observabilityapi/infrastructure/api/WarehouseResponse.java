package com.r2m.observabilityapi.infrastructure.api;

import com.r2m.observabilityapi.infrastructure.persistence.Shelve;
import com.r2m.observabilityapi.infrastructure.persistence.Warehouse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class WarehouseResponse {

  private Warehouse warehouse;

  private List<Shelve> shelves;
}
