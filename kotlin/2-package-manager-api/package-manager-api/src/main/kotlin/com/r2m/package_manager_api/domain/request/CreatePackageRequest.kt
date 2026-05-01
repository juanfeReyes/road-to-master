package com.r2m.package_manager_api.domain.request

import com.r2m.package_manager_api.domain.model.parts.PartCategory
import java.util.UUID

data class CreatePackageRequest(
    var content: MutableList<String>,
    var category: PartCategory,
    var initialVersion: String
) {

}