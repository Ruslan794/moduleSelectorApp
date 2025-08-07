package dev.rr.moduleselectorapp.selector.model

import dev.rr.moduleselectorapp.common.BaseEntity
import dev.rr.moduleselectorapp.subject.model.AbroadSemester
import dev.rr.moduleselectorapp.subject.model.Course
import dev.rr.moduleselectorapp.subject.model.Subject
import dev.rr.moduleselectorapp.survey.model.SurveyData
import jakarta.persistence.*

@Entity
@Table(name = "student_selections")
class StudentSelection : BaseEntity() {

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "survey_data_id", nullable = false)
    lateinit var surveyData: SurveyData

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selected_subject_id")
    var selectedSubject: Subject? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "student_optional_course_selections",
        joinColumns = [JoinColumn(name = "selection_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    var selectedOptionalCourses: MutableList<Course> = mutableListOf()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selected_abroad_semester_id")
    var selectedAbroadSemester: AbroadSemester? = null

    @Column(name = "is_completed")
    var isCompleted: Boolean = false

    override fun toString(): String {
        return "StudentSelection(id=$id, subject=${selectedSubject?.name}, completed=$isCompleted)"
    }
}