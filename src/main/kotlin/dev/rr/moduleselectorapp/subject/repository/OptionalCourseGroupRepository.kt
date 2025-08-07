package dev.rr.moduleselectorapp.subject.repository

import dev.rr.moduleselectorapp.subject.model.OptionalCourseGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OptionalCourseGroupRepository : JpaRepository<OptionalCourseGroup, UUID>{

    fun findBySemester(semester: Int): List<OptionalCourseGroup>
}