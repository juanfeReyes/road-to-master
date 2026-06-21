package com.r2m.application.mappers;

import com.r2m.domain.model.Package;
import com.r2m.infrastructure.persistence.entity.PackageEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface PackageMapper {

    Package toDomain(PackageEntity entity);
    List<Package> toDomainList(List<PackageEntity> entity);

    PackageEntity toEntity(Package domain);
    List<PackageEntity> toEntityList(List<Package> domain);
}
