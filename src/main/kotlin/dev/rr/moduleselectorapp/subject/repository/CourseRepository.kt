package dev.rr.moduleselectorapp.subject.repository

import dev.rr.moduleselectorapp.subject.model.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CourseRepository : JpaRepository<Course, UUID>{

    fun findBySemester(semester: Int): List<Course>

    fun findByCode(code: String): Course?

    fun findByNameContainingIgnoreCase(name: String): List<Course>

    fun findBySubjectId(subjectId: UUID): List<Course>

    fun findByOptionalGroupId(optionalGroupId: UUID): List<Course>

    fun findByAbroadSemesterId(abroadSemesterId: UUID): List<Course>

    @Query("SELECT c FROM Course c WHERE c.subject.id = :subjectId AND c.isCompulsory = true AND c.optionalGroup IS NULL AND c.abroadSemester IS NULL")
    fun findCompulsoryCoursesBySubjectId(subjectId: UUID): List<Course>
}