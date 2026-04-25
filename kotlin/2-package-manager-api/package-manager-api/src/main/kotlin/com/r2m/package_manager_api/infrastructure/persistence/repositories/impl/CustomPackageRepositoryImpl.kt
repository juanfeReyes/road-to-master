package com.r2m.package_manager_api.infrastructure.persistence.repositories.impl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageStorageEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

class CustomPackageRepositoryImpl(
    @PersistenceContext val entityManager: EntityManager
): CustomPackageRepository {

    override fun search(criteria: SearchPackageCriteria): List<PackageStorageEntity> {
        val context = JpqlRenderContext()
        val query = jpql {
            select(
                entity(PackageStorageEntity::class)
            ).from(
                entity(PackageStorageEntity::class)
            ).where(
                and(
                    criteria.category?.let { path(PackageStorageEntity::category).eq(criteria.category) }
                )
            )

        }
        val tQuery = entityManager.createQuery(query, context)
        tQuery.setFirstResult(criteria.page)
        tQuery.setMaxResults(criteria.size)
        return tQuery.resultList
    }
}