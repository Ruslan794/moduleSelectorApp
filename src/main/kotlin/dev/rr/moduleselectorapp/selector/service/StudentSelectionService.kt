package dev.rr.moduleselectorapp.selector.service

import dev.rr.moduleselectorapp.selector.model.StudentSelection
import dev.rr.moduleselectorapp.selector.repository.StudentSelectionRepository
import dev.rr.moduleselectorapp.subject.model.AbroadSemester
import dev.rr.moduleselectorapp.subject.model.Course
import dev.rr.moduleselectorapp.subject.model.Subject
import dev.rr.moduleselectorapp.subject.service.AbroadSemesterService
import dev.rr.moduleselectorapp.subject.service.CourseService
import dev.rr.moduleselectorapp.subject.service.SubjectService
import dev.rr.moduleselectorapp.survey.model.SurveyData
import dev.rr.moduleselectorapp.survey.service.SurveyService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class StudentSelectionService(
    private val studentSelectionRepository: StudentSelectionRepository,
    private val surveyService: SurveyService,
    private val subjectService: SubjectService,
    private val courseService: CourseService,
    private val abroadSemesterService: AbroadSemesterService,
    private val httpSession: HttpSession
) {

    object SessionKeys {
        const val STUDENT_SELECTION = "STUDENT_SELECTION"
    }

    @Transactional
    fun initializeFromSession(): StudentSelection? {
        val surveyData = surveyService.getFromSession() ?: return null

        var selection = getFromSession()
        if (selection == null) {
            selection = StudentSelection().apply {
                this.surveyData = surveyData
            }
            saveToSession(selection)
        }

        if (selection.selectedSubject != null && selection.selectedAbroadSemester == null) {
            val abroadOptions = selection.selectedSubject!!.abroadSemestersToChoose
            if (abroadOptions.size == 1) {
                selectAbroadSemester(abroadOptions[0].id!!)
            }
        }

        return selection
    }

    fun saveToSession(selection: StudentSelection) {
        httpSession.setAttribute(SessionKeys.STUDENT_SELECTION, selection)
    }

    fun getFromSession(): StudentSelection? {
        return httpSession.getAttribute(SessionKeys.STUDENT_SELECTION) as? StudentSelection
    }

    fun clearSession() {
        httpSession.removeAttribute(SessionKeys.STUDENT_SELECTION)
    }

    @Transactional
    fun selectSubject(subjectId: UUID): StudentSelection? {
        val selection = getFromSession() ?: return null
        val subject = subjectService.getSubjectById(subjectId)

        if (subject != null) {
            selection.selectedSubject = subject
            selection.selectedOptionalCourses.clear()
            selection.selectedAbroadSemester = null

            autoSelectDefaults(selection, subject)
            saveToSession(selection)
        }
        return selection
    }

    private fun autoSelectDefaults(selection: StudentSelection, subject: Subject) {
        if (subject.abroadSemestersToChoose.isNotEmpty()) {
            val firstAbroadSemester = subject.abroadSemestersToChoose.first()
            selection.selectedAbroadSemester = firstAbroadSemester

            subject.optionalCourseGroups.forEach { group ->
                if (group.coursesToChoose.isNotEmpty()) {
                    selection.selectedOptionalCourses.add(group.coursesToChoose.first())
                }
            }

            firstAbroadSemester.optionalCourseGroups.forEach { group ->
                if (group.coursesToChoose.isNotEmpty()) {
                    selection.selectedOptionalCourses.add(group.coursesToChoose.first())
                }
            }
        } else {
            subject.optionalCourseGroups.forEach { group ->
                if (group.coursesToChoose.isNotEmpty()) {
                    selection.selectedOptionalCourses.add(group.coursesToChoose.first())
                }
            }
        }
    }

    @Transactional
    fun selectOptionalCourse(courseId: UUID): StudentSelection? {
        val selection = getFromSession() ?: return null
        val course = courseService.getCourseById(courseId)

        if (course != null) {
            val optionalGroup = course.optionalGroup
            if (optionalGroup != null) {
                selection.selectedOptionalCourses.removeIf { existingCourse ->
                    existingCourse.optionalGroup?.id == optionalGroup.id
                }

                if (!selection.selectedOptionalCourses.any { it.id == course.id }) {
                    selection.selectedOptionalCourses.add(course)
                }
                saveToSession(selection)
            }
        }
        return selection
    }

    @Transactional
    fun selectAbroadSemester(abroadSemesterId: UUID): StudentSelection? {
        val selection = getFromSession() ?: return null
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(abroadSemesterId)

        if (abroadSemester != null) {
            selection.selectedAbroadSemester = abroadSemester
            selection.selectedOptionalCourses.removeIf { course ->
                course.abroadSemester != null ||
                        (course.optionalGroup != null && course.abroadSemester == null)
            }
            saveToSession(selection)
        }
        return selection
    }

    @Transactional
    fun selectAbroadOptionalCourse(courseId: UUID): StudentSelection? {
        val selection = getFromSession() ?: return null
        val course = courseService.getCourseById(courseId)

        if (course != null && course.abroadSemester?.id == selection.selectedAbroadSemester?.id) {
            val optionalGroup = course.optionalGroup
            if (optionalGroup != null) {
                selection.selectedOptionalCourses.removeIf { existingCourse ->
                    existingCourse.optionalGroup?.id == optionalGroup.id && existingCourse.abroadSemester != null
                }

                if (!selection.selectedOptionalCourses.any { it.id == course.id }) {
                    selection.selectedOptionalCourses.add(course)
                }
                saveToSession(selection)
            }
        }
        return selection
    }

    @Transactional
    fun finalizeSelection(): StudentSelection? {
        val selection = getFromSession() ?: return null

        val persistedSurvey = surveyService.persistSurveyData()
        if (persistedSurvey != null) {
            selection.surveyData = persistedSurvey
            selection.isCompleted = true

            val savedSelection = studentSelectionRepository.save(selection)
            clearSession()
            surveyService.clearSession()
            return savedSelection
        }
        return null
    }

    @Transactional(readOnly = true)
    fun getAllCompletedSelections(): List<StudentSelection> {
        return studentSelectionRepository.findByIsCompletedTrue()
    }
}