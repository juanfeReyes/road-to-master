package com.r2m.package_manager_api.infrastructure.persistence.repositories

import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageEntity
import org.springframework.data.repository.ListCrudRepository

interface IPackageRepository : ListCrudRepository<PackageEntity, String> {
}