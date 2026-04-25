package com.r2m.package_manager_api.application.mapper

import com.r2m.package_manager_api.domain.model.Part
import com.r2m.package_manager_api.infrastructure.persistence.entity.PartItemEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PartMapper {

    fun toDomain(entity: PartItemEntity): Part

    fun toEntity(domain: Part): PartItemEntity
}