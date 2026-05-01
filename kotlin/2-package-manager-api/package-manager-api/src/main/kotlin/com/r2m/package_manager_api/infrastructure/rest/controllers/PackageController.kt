package com.r2m.package_manager_api.infrastructure.rest.controllers

import com.r2m.package_manager_api.application.mapper.PackageMapper
import com.r2m.package_manager_api.application.services.IPackageService
import com.r2m.package_manager_api.domain.model.packages.PackageStorage
import com.r2m.package_manager_api.domain.request.CreatePackageRequest
import com.r2m.package_manager_api.domain.request.PublishPackageRequest
import com.r2m.package_manager_api.domain.request.SearchPackageCriteriaV1
import com.r2m.package_manager_api.domain.request.SearchPackageCriteriaV2
import com.r2m.package_manager_api.domain.request.UpdatePackageRequest
import com.r2m.package_manager_api.infrastructure.rest.configuration.ControllerPaths
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController()
@RequestMapping(ControllerPaths.Companion.PACKAGE_URL)
class PackageController(
    val service: IPackageService,
    val mapper: PackageMapper
) {

    @GetMapping("")
    fun searchV1(criteria: SearchPackageCriteriaV1): List<PackageStorage> {
        return service.search(mapper.toCriteria(criteria))
    }

    @GetMapping("", version = "1.1")
    fun searchV1_1(criteria: SearchPackageCriteriaV2): List<PackageStorage> {
        return service.search(mapper.toCriteria(criteria))
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