package dev.rr.moduleselectorapp.subject.service

import dev.rr.moduleselectorapp.subject.model.Course
import dev.rr.moduleselectorapp.subject.repository.CourseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CourseService(
    private val courseRepository: CourseRepository
) {

    @Transactional(readOnly = true)
    fun getAllCourses(): List<Course> {
        return courseRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getCoursesBySemester(semester: Int): List<Course> {
        return courseRepository.findBySemester(semester)
    }

    @Transactional(readOnly = true)
    fun getCourseById(id: UUID): Course? {
        return courseRepository.findById(id).orElse(null)
    }

    @Transactional(readOnly = true)
    fun getCourseByCode(code: String): Course? {
        return courseRepository.findByCode(code)
    }

    @Transactional(readOnly = true)
    fun getCoursesBySubject(subjectId: UUID): List<Course> {
        return courseRepository.findBySubjectId(subjectId)
    }

    @Transactional(readOnly = true)
    fun getCompulsoryCoursesBySubject(subjectId: UUID): List<Course> {
        return courseRepository.findCompulsoryCoursesBySubjectId(subjectId)
    }

    @Transactional(readOnly = true)
    fun getCoursesByOptionalGroup(optionalGroupId: UUID): List<Course> {
        return courseRepository.findByOptionalGroupId(optionalGroupId)
    }

    @Transactional(readOnly = true)
    fun getCoursesByAbroadSemester(abroadSemesterId: UUID): List<Course> {
        return courseRepository.findByAbroadSemesterId(abroadSemesterId)
    }

    @Transactional
    fun saveCourse(course: Course): Course {
        return courseRepository.save(course)
    }

    @Transactional
    fun deleteCourse(id: UUID) {
        courseRepository.deleteById(id)
    }
}