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
@Table(name = "subjects")
class Subject : BaseEntity() {

    @field:NotBlank(message = "Subject name is required")
    @field:Size(min = 2, message = "Subject name must be at least 2 characters")
    var name: String = ""

    @ManyToMany
    @JoinTable(
        name = "subject_compulsory_courses",
        joinColumns = [JoinColumn(name = "subject_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    var compulsoryCourses: MutableList<Course> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "subject_id")
    var optionalCourseGroups: MutableList<OptionalCourseGroup> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "subject_id")
    var abroadSemestersToChoose: MutableList<AbroadSemester> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_abroad_semester_id")
    var selectedAbroadSemester: AbroadSemester? = null

    override fun toString(): String {
        return "Subject(id=$id, name='$name', selectedAbroadSemester=${selectedAbroadSemester?.university?.name})"
    }
}
