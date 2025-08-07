package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank


@Entity
@Table(name = "optional_course_groups")
class OptionalCourseGroup : BaseEntity() {

    @Column(name = "semester_number")
    var semester: Int = 1

    @field:NotBlank(message = "Group name is required")
    var name: String = ""

    @ManyToMany
    @JoinTable(
        name = "optional_course_group_courses",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    var coursesToChoose: MutableList<Course> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_course_id")
    var selection: Course? = null

    override fun toString(): String {
        return "OptionalCourseGroup(id=$id, semester=$semester, name='$name')"
    }
}
