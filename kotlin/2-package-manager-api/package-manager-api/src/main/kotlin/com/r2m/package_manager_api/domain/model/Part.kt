package com.r2m.package_manager_api.domain.model

import java.util.UUID

data class Part(
    val id: UUID,
    val name: String
) : IStorable {
}