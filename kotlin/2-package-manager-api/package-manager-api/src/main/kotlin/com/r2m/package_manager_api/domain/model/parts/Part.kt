package com.r2m.package_manager_api.domain.model.parts

import com.r2m.package_manager_api.domain.model.packages.IStorable
import java.util.UUID

data class Part(
    val id: UUID,
    val name: String
) : IStorable {
}