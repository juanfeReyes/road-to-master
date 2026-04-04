package com.r2m.package_manager_api.domain.model

import java.util.UUID

data class Part(override val id: UUID, override val name: String): ContentItem(id, name) {
}