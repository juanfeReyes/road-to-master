package com.r2m.package_manager_api.domain.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    visible = true)
@JsonSubTypes(value = [
    JsonSubTypes.Type(value = PackageStorage::class, name = "PACKAGE_STORAGE"),
    JsonSubTypes.Type(value = Part::class, name = "PART")
])
interface IStorable {
}