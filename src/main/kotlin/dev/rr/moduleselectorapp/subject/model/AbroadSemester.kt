package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "abroad_semesters")
class AbroadSemester : BaseEntity() {

    @Column(name = "semester_number")
    var semester: Int = 1

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id", nullable = false)
    lateinit var university: University

    @OneToMany(mappedBy = "abroadSemester", cascade = [CascadeType.ALL], orphanRemoval = true)
    var courses: MutableList<Course> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "abroad_semester_id")
    var optionalCourseGroups: MutableList<OptionalCourseGroup> = mutableListOf()

    fun getCompulsoryCourses(): List<Course> {
        return courses.filter { it.isCompulsory }
    }

    override fun toString(): String {
        return "AbroadSemester(id=$id, semester=$semester, university='${university.name}')"
    }
}