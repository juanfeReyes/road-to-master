package com.r2m.package_manager_api.application.services.impl

import com.r2m.package_manager_api.application.mapper.PackageMapper
import com.r2m.package_manager_api.application.services.IPackageService
import com.r2m.package_manager_api.domain.model.PackageStorage
import com.r2m.package_manager_api.domain.request.CreatePackageRequest
import com.r2m.package_manager_api.domain.request.PublishPackageRequest
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.domain.request.UpdatePackageRequest
import com.r2m.package_manager_api.infrastructure.persistence.repositories.PackageRepository
import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.nextMajor
import io.github.z4kn4fein.semver.nextMinor
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class PackagesService(
    val packageRepository: PackageRepository,
    val packageMapper: PackageMapper
): IPackageService {

    override fun search(criteria: SearchPackageCriteria): List<PackageStorage> {
        return packageMapper.toDomainList(packageRepository.search(criteria))
    }

    override fun create(request: CreatePackageRequest) {
        var contentItems = packageMapper.toDomainList(packageRepository.search(SearchPackageCriteria(ids = request.content)))
        var pack = PackageStorage(items = contentItems,
            id = UUID.randomUUID(),
            category = request.category,
            version = Version.parse(request.initialVersion))

        packageRepository.save(packageMapper.toEntity(pack))
    }

    override fun update(request: UpdatePackageRequest) {
        val pack = when(val entity = packageRepository.findById(request.id.toString()).getOrNull()) {
            null -> throw Exception("Package does not exist")
            else -> {packageMapper.toDomain(entity)}
        }

        var contentItems = packageMapper.toDomainList(packageRepository.search(SearchPackageCriteria(ids = request.content)))
        pack.category = request.category
        pack.version = pack.version.nextMinor()
        pack.items = contentItems

        packageRepository.save(packageMapper.toEntity(pack))
    }

    override fun publish(request: PublishPackageRequest) {
        val pack = when(val entity = packageRepository.findById(request.id.toString()).getOrNull()) {
            null -> throw Exception("Package does not exist")
            else -> {packageMapper.toDomain(entity)}
        }
        pack.version = pack.version.nextMajor()

        packageRepository.save(packageMapper.toEntity(pack))
    }

    override fun delete(id: UUID) {
        packageRepository.deleteById(id.toString())
    }

}