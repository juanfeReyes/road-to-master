package com.r2m.package_manager_api.application.services.impl

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isInstanceOf
import com.r2m.package_manager_api.application.mapper.PackageMapper
import com.r2m.package_manager_api.application.mapper.PackageMapperImpl
import com.r2m.package_manager_api.application.mapper.PartMapper
import com.r2m.package_manager_api.application.mapper.PartMapperImpl
import com.r2m.package_manager_api.domain.model.packages.PackageStorage
import com.r2m.package_manager_api.domain.model.packages.SearchCriteria
import com.r2m.package_manager_api.domain.model.parts.Part
import com.r2m.package_manager_api.domain.request.SearchPackageCriteriaV1
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageStorageEntity
import com.r2m.package_manager_api.infrastructure.persistence.entity.PartItemEntity
import com.r2m.package_manager_api.infrastructure.persistence.repositories.PackageRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class PackagesServiceTest {
    @MockK
    lateinit var packageRepository: PackageRepository

    @SpyK
    var partMapper: PartMapper = PartMapperImpl()

    @SpyK
    var packageMapper: PackageMapper = PackageMapperImpl(partMapper)

    @OverrideMockKs
    lateinit var service: PackagesService

    @Test
    fun `should return search`() {
        val part = PartItemEntity(UUID.randomUUID().toString(), "Part test")
        val innerPackage = PackageStorageEntity(listOf(part), id = UUID.randomUUID().toString())
        val entities = listOf(PackageStorageEntity(listOf(innerPackage), id = UUID.randomUUID().toString()))
        every { packageRepository.search(any()) } returns entities

        val packages = service.search(SearchCriteria())

        verify(exactly = 1) { packageRepository.search(any()) }
        assertThat(packages).hasSize(1)
        assertThat(packages.first().items).hasSize(1)
        assertThat((packages.first().items.first() as PackageStorage).items.first()).isInstanceOf(Part::class)
    }

    @Test
    fun save() {
    }

    @Test
    fun update() {
    }

    @Test
    fun publish() {
    }

    @Test
    fun delete() {
    }

}