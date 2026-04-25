package com.r2m.package_manager_api.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.Table
import org.hibernate.annotations.AnyDiscriminator
import org.hibernate.annotations.AnyDiscriminatorImplicitValues
import org.hibernate.annotations.AnyDiscriminatorValue
import org.hibernate.annotations.AnyKeyJavaClass
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.hibernate.annotations.ManyToAny

@Entity
@Table(name = "package")
class PackageStorageEntity(
    @ManyToAny
    @Cascade(value = [CascadeType.ALL])
    @AnyDiscriminator(DiscriminatorType.STRING)
    @Column(name = "content_type")
    @AnyKeyJavaClass(String::class)
    @AnyDiscriminatorValue(discriminator = "PACKAGE", entity = PackageStorageEntity::class)
    @AnyDiscriminatorValue(discriminator = "PART", entity = PartItemEntity::class)
    @AnyDiscriminatorImplicitValues(AnyDiscriminatorImplicitValues.Strategy.SHORT_NAME)
    @JoinTable(
        name = "package_content",
        joinColumns = [JoinColumn(name = "package_id")],
        inverseJoinColumns = [JoinColumn(name = "content_id")]
    )
    val items: List<IStorableEntity>,
    @Id
    val id: String,
    @Column(name = "version")
    var version: String = "0.0.1",
    @Column(name = "category")
    var category: String = "UNKNOWN"
): IStorableEntity {
}
