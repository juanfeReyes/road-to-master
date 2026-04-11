package com.r2m.package_manager_api.infrastructure.persistence.repositories

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageEntity
import com.r2m.package_manager_api.infrastructure.persistence.repositories.impl.CustomPackageRepository
import org.springframework.data.repository.ListCrudRepository

interface PackageRepository : ListCrudRepository<PackageEntity, String>, CustomPackageRepository, KotlinJdslJpqlExecutor {
}