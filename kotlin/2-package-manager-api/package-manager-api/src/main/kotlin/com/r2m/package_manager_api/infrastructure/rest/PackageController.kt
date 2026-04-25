package com.r2m.package_manager_api.infrastructure.rest

import com.r2m.package_manager_api.application.services.IPackageService
import com.r2m.package_manager_api.domain.model.PackageStorage
import com.r2m.package_manager_api.domain.request.CreatePackageRequest
import com.r2m.package_manager_api.domain.request.PublishPackageRequest
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.domain.request.UpdatePackageRequest
import com.r2m.package_manager_api.infrastructure.rest.ControllerPaths.Companion.PACKAGE_URL
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController()
@RequestMapping(PACKAGE_URL)
class PackageController(
    val service: IPackageService
) {

    @GetMapping("")
    fun search(params: SearchPackageCriteria): List<PackageStorage> {
        return service.search(params)
    }

    @PostMapping("")
    fun create(@RequestBody request: CreatePackageRequest) {
        return service.create(request)
    }

    @PutMapping("")
    fun update(
        @RequestBody request: UpdatePackageRequest
    ) {
        return service.update(request)
    }

    @PatchMapping("")
    fun publish(@RequestBody request: PublishPackageRequest) {
        return service.publish(request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        return service.delete(id)
    }
}