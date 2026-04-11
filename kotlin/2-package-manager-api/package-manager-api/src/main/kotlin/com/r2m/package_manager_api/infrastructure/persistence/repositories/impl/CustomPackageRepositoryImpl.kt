package com.r2m.package_manager_api.infrastructure.persistence.repositories.impl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.r2m.package_manager_api.domain.request.SearchPackageCriteria
import com.r2m.package_manager_api.infrastructure.persistence.entity.PackageEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.Predicate

class CustomPackageRepositoryImpl(
    @PersistenceContext val entityManager: EntityManager
): CustomPackageRepository {

    override fun search(criteria: SearchPackageCriteria): List<PackageEntity> {
        val context = JpqlRenderContext()
        val query = jpql {
            select(
                entity(PackageEntity::class)
            ).from(
                entity(PackageEntity::class)
            ).where(
                and(
                    criteria.category?.let { path(PackageEntity::category).eq(criteria.category) }
                )
            )
        }

        return entityManager.createQuery(query, context).resultList
    }


}