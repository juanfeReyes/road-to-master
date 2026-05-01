package com.r2m.package_manager_api.domain.request

data class SearchPackageCriteriaV1(
    val page: Int = 0,
    val size: Int = 50,
    val ids: List<String> = listOf(),
    val category: String? = null
)
