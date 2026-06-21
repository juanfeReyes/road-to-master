package com.r2m.application.services;

import com.r2m.application.mappers.PackageMapper;
import com.r2m.domain.model.Package;
import com.r2m.infrastructure.persistence.repository.PackageRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PackageService {

    private final PackageRepository repository;
    private final PackageMapper mapper;

    public PackageService(PackageRepository repository, PackageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Uni<List<Package>> search(){
        return repository.find("hehe", "")
                .page(Page.ofSize(25))
                .list()
                .map(mapper::toDomainList);
    }
}
