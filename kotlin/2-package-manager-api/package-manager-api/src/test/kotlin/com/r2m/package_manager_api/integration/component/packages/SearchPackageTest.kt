package com.r2m.package_manager_api.integration.component.packages

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isNotNull
import com.r2m.package_manager_api.application.mapper.PackageMapper
import com.r2m.package_manager_api.domain.model.PackageStorage
import com.r2m.package_manager_api.domain.model.PartCategory
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageStorageEntity
import com.r2m.package_manager_api.infrastructure.persistence.entity.PartItemEntity
import com.r2m.package_manager_api.infrastructure.persistence.repositories.PackageRepository
import com.r2m.package_manager_api.infrastructure.rest.ControllerPaths.Companion.PACKAGE_URL
import com.r2m.package_manager_api.integration.IntegrationTestBase
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import org.springframework.util.LinkedMultiValueMap
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

import java.util.*

class SearchPackageTest(
    @Autowired private val client: RestTestClient,
    @Autowired private val repository: PackageRepository,
    @Autowired private val mapper: ObjectMapper
) : IntegrationTestBase() {

    @BeforeAll
    fun setup() {
        val content = listOf(PartItemEntity(UUID.randomUUID().toString(), "Test item"))
        val entity = PackageStorageEntity(content, UUID.randomUUID().toString())
        val entityWithVersion =
            PackageStorageEntity(content, UUID.randomUUID().toString(), category = PartCategory.INDUSTRIAL.toString())
        repository.saveAll(listOf(entity, entityWithVersion))
    }

    @Test
    fun `should search without parameters`() {
        client.get().uri(PACKAGE_URL)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody<List<PackageStorage>>()
            .consumeWith { result ->
                assertThat(result.responseBody).isNotNull().hasSize(2)
            }
    }

    @Test
    fun `should search by category`() {
        client.get().uri { uriBuilder ->
            uriBuilder.path(PACKAGE_URL)
                .queryParam("category", PartCategory.INDUSTRIAL.toString())
                .build()
        }
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody<List<PackageStorage>>()
            .consumeWith { result ->
                assertThat(result.responseBody).isNotNull().hasSize(1)
            }
    }
}