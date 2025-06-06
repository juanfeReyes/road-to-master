package com.example.DonationManager.application.resource;

import com.example.DonationManager.domain.InventoryLocation;
import com.example.DonationManager.domain.Resource;
import org.springframework.stereotype.Service;

@Service
public class AllocateResourceToStorageFacility {

  private int nextCubicle;

  private final int maxCubicleNumber;

  public AllocateResourceToStorageFacility(){
    nextCubicle = 0;
    maxCubicleNumber = 10;
  }

  //Strategy is round-robin for the inventoryLocation
  public Resource execute(Resource resource){

    var location = new InventoryLocation("Shelter", "A", nextCubicle);
    resource.setInventoryLocation(location);

    nextCubicle = nextCubicle < maxCubicleNumber? nextCubicle + 1: 0;

    return resource;
  }
}
