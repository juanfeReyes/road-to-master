package com.r2m.package_manager_api.domain.model

import java.util.UUID

abstract class ContentItem(open val id: UUID, open val name: String): IStorable {
}