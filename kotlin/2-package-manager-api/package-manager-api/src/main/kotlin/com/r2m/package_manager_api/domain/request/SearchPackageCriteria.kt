package com.r2m.package_manager_api.domain.request

data class SearchPackageCriteria(
    val page: Int = 0,
    val category: String?
)
