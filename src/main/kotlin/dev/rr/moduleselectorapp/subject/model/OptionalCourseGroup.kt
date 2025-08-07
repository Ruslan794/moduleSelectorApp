package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "optional_course_groups")
class OptionalCourseGroup : BaseEntity() {

    @Column(name = "semester_number")
    var semester: Int = 1

    @OneToMany(mappedBy = "optionalGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    var coursesToChoose: MutableList<Course> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_course_id")
    var selection: Course? = null

    override fun toString(): String {
        return "OptionalCourseGroup(id=$id, semester=$semester, coursesCount=${coursesToChoose.size})"
    }
}