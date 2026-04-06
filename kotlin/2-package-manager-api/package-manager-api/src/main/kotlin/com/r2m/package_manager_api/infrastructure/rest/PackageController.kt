package com.r2m.package_manager_api.infrastructure.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PackageController {

    @GetMapping("/")
    fun getPackages(@RequestParam params: Map<String, String>){

    }
}