package com.r2m.package_manager_api.application.services

import com.r2m.package_manager_api.domain.model.PackageStorage
import com.r2m.package_manager_api.domain.request.CreatePackageRequest
import com.r2m.package_manager_api.domain.request.PublishPackageRequest
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.domain.request.UpdatePackageRequest
import java.util.UUID

interface IPackageService {

    fun search(criteria: SearchPackageCriteria): List<PackageStorage>

    fun create(request: CreatePackageRequest)

    fun update(request: UpdatePackageRequest)

    /**
     * Increases Major version
     */
    fun publish(request: PublishPackageRequest)

    fun delete(id: UUID)
}