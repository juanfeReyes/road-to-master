package com.r2m.package_manager_api.domain.request

data class SearchPackageCriteriaV2(
    val page: Int = 0,
    val size: Int = 50,
    val ids: List<String>? = null,
    val categories: List<String>? = null
)
