package com.r2m.observabilityapi.infrastructure;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class CustomObservationHandler implements ObservationHandler<Observation.Context> {

  private static final Logger log = LoggerFactory.getLogger(CustomObservationHandler.class);

  @Override
  public void onStart(Observation.Context context) {
    log.info("Before running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
  }

  @Override
  public void onStop(Observation.Context context) {
    log.info("After running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
  }

  @Override
  public boolean supportsContext(Observation.Context context) {
    return true;
  }

  private String getUserTypeFromContext(Observation.Context context) {
    return "juan";
  }
}
