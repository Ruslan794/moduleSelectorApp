package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


@Entity
@Table(name = "abroad_semesters")
class AbroadSemester : BaseEntity() {

    @Column(name = "semester_number")
    var semester: Int = 1

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id", nullable = false)
    lateinit var university: University

    @ManyToMany
    @JoinTable(
        name = "abroad_semester_compulsory_courses",
        joinColumns = [JoinColumn(name = "abroad_semester_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    var compulsoryCourses: MutableList<Course> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "abroad_semester_id")
    var optionalCourseGroups: MutableList<OptionalCourseGroup> = mutableListOf()

    override fun toString(): String {
        return "AbroadSemester(id=$id, semester=$semester, university='${university.name}')"
    }
}