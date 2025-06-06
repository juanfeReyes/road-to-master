package com.road.master.PetShelter.domain.valueObjects;

import com.road.master.PetShelter.domain.exceptions.MissingArgumentException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Name {

  private String value;

  public Name(String name) {
    if (StringUtils.isBlank(name)) throw new MissingArgumentException("Name");
    this.value = name;
  }
}
