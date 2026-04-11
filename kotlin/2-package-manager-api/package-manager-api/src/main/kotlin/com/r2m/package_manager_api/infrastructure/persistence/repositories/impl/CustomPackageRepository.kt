package com.r2m.package_manager_api.infrastructure.persistence.repositories.impl

import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageEntity

interface CustomPackageRepository {

    fun search(criteria: SearchPackageCriteria): List<PackageEntity>
}