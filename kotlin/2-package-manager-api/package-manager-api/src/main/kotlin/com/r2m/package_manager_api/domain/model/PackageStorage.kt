package com.r2m.package_manager_api.domain.model

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion
import java.util.*

class PackageStorage(
    var items: List<IStorable>,
    val id: UUID = UUID.randomUUID(),
    var version: Version = "0.0.1".toVersion(),
    var category: PartCategory = PartCategory.UNKNOWN,
) : IStorable {

    init {
        check(!items.isEmpty()) { "Package has to contain at least one item" }
    }

}