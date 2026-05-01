package com.r2m.package_manager_api.domain.model.packages

data class SearchCriteria(
    val page: Int = 0,
    val size: Int = 50,
    val ids: MutableList<String> = mutableListOf(),
    val categories: MutableList<String> = mutableListOf()
)