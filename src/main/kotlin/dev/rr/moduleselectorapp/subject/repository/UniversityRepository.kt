package dev.rr.moduleselectorapp.subject.repository


import dev.rr.moduleselectorapp.subject.model.University
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UniversityRepository : JpaRepository<University, UUID>{

    fun findByName(name: String): University?

    fun findByShortName(shortName: String): University?

    fun findByCountry(country: String): List<University>
}
