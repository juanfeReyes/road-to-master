package com.r2m.package_manager_api.integration.persistence

import assertk.assertThat
import assertk.assertions.hasSize
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageEntity
import com.r2m.package_manager_api.infrastructure.persistence.entity.PartItemEntity
import com.r2m.package_manager_api.infrastructure.persistence.repositories.PackageRepository
import com.r2m.package_manager_api.integration.IntegrationTestBase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class PackageRepositoryTest(
    @Autowired val packageRepository: PackageRepository
): IntegrationTestBase() {

    @BeforeEach
    fun setup(){
        packageRepository.deleteAll()
    }

    @Test
    fun `should create a package with single part`() {
        val content = listOf(PartItemEntity(UUID.randomUUID().toString(), "Test item"))
        val entity = PackageEntity(content, UUID.randomUUID().toString())
        packageRepository.save(entity)
        val packages = packageRepository.findAll();
        assertThat(packages).hasSize(1)
    }

    @Test
    fun `should create a package with package and single part`() {
        val innerContent = listOf(PartItemEntity(UUID.randomUUID().toString(), "Inner Test item"))
        val innerPackage = PackageEntity(innerContent, UUID.randomUUID().toString())
        val content = listOf(innerPackage, PartItemEntity(UUID.randomUUID().toString(), "Test item"))
        val entity = PackageEntity(content, UUID.randomUUID().toString())
        packageRepository.save(entity)
        val packages = packageRepository.findAll();
        assertThat(packages).hasSize(2)
    }

    @Test
    fun `should create package with existing package`() {
        val innerContent = listOf(PartItemEntity(UUID.randomUUID().toString(), "Inner Test item"))
        val innerPackage = PackageEntity(innerContent, UUID.randomUUID().toString())
        packageRepository.save(innerPackage)
        val savedPackage = packageRepository.findById(innerPackage.id).get()

        val content = listOf(savedPackage, PartItemEntity(UUID.randomUUID().toString(), "Test item"))
        val entity = PackageEntity(content, UUID.randomUUID().toString())
        packageRepository.save(entity)

        val packages = packageRepository.findAll();
        assertThat(packages).hasSize(2)
    }

    @Test
    fun `should search packages`() {
        val innerContent = listOf(PartItemEntity(UUID.randomUUID().toString(), "Inner Test item"))
        val innerPackage = PackageEntity(innerContent, UUID.randomUUID().toString())
        val content = listOf(innerPackage, PartItemEntity(UUID.randomUUID().toString(), "Test item"))
        val entity = PackageEntity(content, UUID.randomUUID().toString(), version = "0.2.1", category = "INDUSTRIAL")
        packageRepository.save(entity)
        val packages = packageRepository.search(SearchPackageCriteria(category = "INDUSTRIAL"));
        assertThat(packages).hasSize(1)
    }
}
