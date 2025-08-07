package dev.rr.moduleselectorapp.subject.repository

import dev.rr.moduleselectorapp.subject.model.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CourseRepository : JpaRepository<Course, UUID>{


    fun findBySemester(semester: Int): List<Course>


    fun findByCode(code: String): Course?


    fun findByNameContainingIgnoreCase(name: String): List<Course>
}