package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


@Entity
@Table(name = "universities")
class University : BaseEntity() {

    @field:NotBlank(message = "University name is required")
    var name: String = ""

    @field:NotBlank(message = "Short name is required")
    @Column(name = "short_name")
    var shortName: String = ""

    @field:NotBlank(message = "Country is required")
    var country: String = ""

    override fun toString(): String {
        return "University(id=$id, name='$name', shortName='$shortName', country='$country')"
    }
}