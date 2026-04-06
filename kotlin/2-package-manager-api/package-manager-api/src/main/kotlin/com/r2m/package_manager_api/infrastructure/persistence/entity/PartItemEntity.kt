package com.r2m.package_manager_api.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "part")
class PartItemEntity(
    @Id
    val id: String,
    @Column
    val name: String
): IStorableEntity {
}