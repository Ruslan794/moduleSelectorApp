package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "subjects")
class Subject : BaseEntity() {

    @field:NotBlank(message = "Subject name is required")
    @field:Size(min = 2, message = "Subject name must be at least 2 characters")
    var name: String = ""

    @OneToMany(mappedBy = "subject", cascade = [CascadeType.ALL], orphanRemoval = true)
    var courses: MutableList<Course> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "subject_id")
    var optionalCourseGroups: MutableList<OptionalCourseGroup> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "subject_id")
    var abroadSemestersToChoose: MutableList<AbroadSemester> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_abroad_semester_id")
    var selectedAbroadSemester: AbroadSemester? = null

    fun getCompulsoryCourses(): List<Course> {
        return courses.filter { it.isCompulsory && it.optionalGroup == null && it.abroadSemester == null }
    }

    override fun toString(): String {
        return "Subject(id=$id, name='$name', selectedAbroadSemester=${selectedAbroadSemester?.university?.name})"
    }
}