package com.r2m.infrastructure.api.rest;

import com.r2m.application.services.PackageService;
import com.r2m.domain.model.Package;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/package")
public class PackageController {

    private  final PackageService service;

    public PackageController(PackageService service) {
        this.service = service;
    }

    @GET
    public Uni<List<Package>> search() {
        return service.search();
    }
}
