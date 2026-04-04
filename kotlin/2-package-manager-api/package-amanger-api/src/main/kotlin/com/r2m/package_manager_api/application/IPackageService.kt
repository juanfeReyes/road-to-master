package com.r2m.package_manager_api.application

import com.r2m.package_manager_api.domain.request.CreatePackageRequest
import com.r2m.package_manager_api.domain.request.PublishPackageRequest
import com.r2m.package_manager_api.domain.request.UpdatePackageRequest
import java.util.UUID

interface IPackageService {

    fun search(params: Map<String, String>)

    fun save(request: CreatePackageRequest)

    fun update(request: UpdatePackageRequest)

    /**
     * Increases Major version
     */
    fun publish(request: PublishPackageRequest)

    fun delete(id: UUID)
}