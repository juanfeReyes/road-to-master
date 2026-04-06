package com.r2m.package_manager_api.application.impl

import com.r2m.package_manager_api.application.IPackageService
import com.r2m.package_manager_api.domain.request.CreatePackageRequest
import com.r2m.package_manager_api.domain.request.PublishPackageRequest
import com.r2m.package_manager_api.domain.request.UpdatePackageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PackagesService: IPackageService {

    override fun search(params: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override fun save(request: CreatePackageRequest) {
        TODO("Not yet implemented")
    }

    override fun update(request: UpdatePackageRequest) {
        TODO("Not yet implemented")
    }

    override fun publish(request: PublishPackageRequest) {
        TODO("Not yet implemented")
    }

    override fun delete(id: UUID) {
        TODO("Not yet implemented")
    }

}