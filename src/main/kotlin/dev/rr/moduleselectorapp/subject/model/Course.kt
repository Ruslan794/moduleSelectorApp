package dev.rr.moduleselectorapp.subject.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "courses")
class Course : BaseEntity() {

    @field:NotBlank(message = "Course name is required")
    @field:Size(min = 2, message = "Course name must be at least 2 characters")
    var name: String = ""

    @field:NotBlank(message = "Course code is required")
    var code: String = ""

    @Column(name = "semester_number")
    var semester: Int = 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    var subject: Subject? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "optional_group_id")
    var optionalGroup: OptionalCourseGroup? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abroad_semester_id")
    var abroadSemester: AbroadSemester? = null

    @Column(name = "is_compulsory")
    var isCompulsory: Boolean = false

    override fun toString(): String {
        return "Course(id=$id, name='$name', code='$code', semester=$semester, isCompulsory=$isCompulsory)"
    }
}