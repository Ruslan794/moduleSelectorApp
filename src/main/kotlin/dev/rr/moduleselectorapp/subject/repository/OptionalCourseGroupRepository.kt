package dev.rr.moduleselectorapp.subject.repository

import dev.rr.moduleselectorapp.subject.model.OptionalCourseGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OptionalCourseGroupRepository : JpaRepository<OptionalCourseGroup, UUID>{

    /**
     * Find optional course groups by semester
     */
    fun findBySemester(semester: Int): List<OptionalCourseGroup>

    /**
     * Find optional course groups by name containing the given string
     */
    fun findByNameContainingIgnoreCase(name: String): List<OptionalCourseGroup>
}
