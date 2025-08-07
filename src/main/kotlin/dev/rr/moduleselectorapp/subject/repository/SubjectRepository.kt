package dev.rr.moduleselectorapp.subject.repository

import dev.rr.moduleselectorapp.subject.model.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SubjectRepository : JpaRepository<Subject, UUID>{
    @Query("SELECT DISTINCT s FROM Subject s " +
            "LEFT JOIN FETCH s.compulsoryCourses " +
            "ORDER BY s.name")
    fun findAllWithCompulsoryCourses(): List<Subject>

    fun findByName(name: String): Subject?
}
