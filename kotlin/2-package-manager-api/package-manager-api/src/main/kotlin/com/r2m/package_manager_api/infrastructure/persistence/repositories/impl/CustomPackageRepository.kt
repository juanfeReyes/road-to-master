package com.r2m.package_manager_api.infrastructure.persistence.repositories.impl

import com.r2m.package_manager_api.domain.model.packages.SearchCriteria
import com.r2m.package_manager_api.domain.request.SearchPackageCriteriaV1
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageStorageEntity

interface CustomPackageRepository {

    fun search(criteria: SearchCriteria): List<PackageStorageEntity>
}