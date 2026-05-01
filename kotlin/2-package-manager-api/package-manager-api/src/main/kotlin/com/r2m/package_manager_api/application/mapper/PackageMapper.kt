package com.r2m.package_manager_api.application.mapper

import com.r2m.package_manager_api.domain.model.packages.IStorable
import com.r2m.package_manager_api.domain.model.packages.PackageStorage
import com.r2m.package_manager_api.domain.model.packages.SearchCriteria
import com.r2m.package_manager_api.domain.model.parts.Part
import com.r2m.package_manager_api.domain.request.SearchPackageCriteriaV1
import com.r2m.package_manager_api.domain.request.SearchPackageCriteriaV2
import com.r2m.package_manager_api.infrastructure.persistence.entity.IStorableEntity
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageStorageEntity
import com.r2m.package_manager_api.infrastructure.persistence.entity.PartItemEntity
import io.github.z4kn4fein.semver.Version
import org.mapstruct.Builder
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.SubclassExhaustiveStrategy
import org.mapstruct.SubclassMapping
import org.springframework.util.LinkedMultiValueMap

@Mapper(componentModel = "spring", uses = [PartMapper::class],
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = Builder(disableBuilder = true))
interface PackageMapper {

    @Mapping(source = "version", target = "version", qualifiedByName = ["stringToVersion"])
    fun toDomain(entity: PackageStorageEntity): PackageStorage
    fun toDomainList(entities: List<PackageStorageEntity>): List<PackageStorage>

    @Mapping(source = "version", target = "version", qualifiedByName = ["versionToString"])
    fun toEntity(domain: PackageStorage): PackageStorageEntity

    @SubclassMapping(source = PackageStorageEntity::class, target = PackageStorage::class)
    @SubclassMapping(source = PartItemEntity::class, target = Part::class)
    fun toStorable(value: IStorableEntity): IStorable

    fun toStorableList(list: List<IStorableEntity>): List<IStorable>

    @SubclassMapping(source = PackageStorage::class, target = PackageStorageEntity::class)
    @SubclassMapping(source = Part::class, target = PartItemEntity::class)
    fun toStorableEntity(value: IStorable): IStorableEntity

    fun toStorableEntityList(list: List<IStorable>): List<IStorableEntity>

    fun toMap(criteria: SearchPackageCriteriaV1): LinkedMultiValueMap<String, String>

    @Mapping(source = "category", target = "categories")
    fun toCriteria(request: SearchPackageCriteriaV1): SearchCriteria

    fun toCriteria(request: SearchPackageCriteriaV2): SearchCriteria

    @Named("versionToString")
    fun versionToString(version: Version): String {
        return version.toString()
    }

    @Named("stringToVersion")
    fun stringToVersion(version: String): Version {
        return Version.parse(version)
    }

    fun stringToList(value: String?): MutableList<String>? {
        if (value == null) {
            return null
        }

        return mutableListOf(value)
    }
}