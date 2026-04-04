package com.r2m.package_manager_api.domain.request

import com.r2m.package_manager_api.domain.model.PartCategory
import java.util.UUID

data class UpdatePackageRequest(
    val id: UUID,
    var content: List<UUID>,
    var category: PartCategory,
) {
}