package dev.rr.moduleselectorapp.subject.repository

import dev.rr.moduleselectorapp.subject.model.AbroadSemester
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AbroadSemesterRepository : JpaRepository<AbroadSemester, UUID> {


    fun findBySemester(semester: Int): List<AbroadSemester>

    fun findByUniversityId(universityId: UUID): List<AbroadSemester>
}