package com.r2m.package_manager_api.domain.request

import com.r2m.package_manager_api.domain.model.PartCategory
import java.util.UUID

data class CreatePackageRequest(
    var content: List<String>,
    var category: PartCategory,
    var initialVersion: String
) {

}