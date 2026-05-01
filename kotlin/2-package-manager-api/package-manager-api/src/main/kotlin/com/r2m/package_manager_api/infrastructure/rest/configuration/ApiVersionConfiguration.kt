package com.r2m.package_manager_api.infrastructure.rest.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ApiVersionConfiguration: WebMvcConfigurer {
    companion object {
        const val VERSION_HEADER = "X-API-Version"
    }

    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer.setDefaultVersion("1.0")
            .useRequestHeader(VERSION_HEADER)
        super.configureApiVersioning(configurer)
    }
}