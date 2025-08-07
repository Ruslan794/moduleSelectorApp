package dev.rr.moduleselectorapp.subject.service

import dev.rr.moduleselectorapp.subject.model.University
import dev.rr.moduleselectorapp.subject.repository.UniversityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UniversityService(
    private val universityRepository: UniversityRepository
) {

    /**
     * Get all universities
     */
    @Transactional(readOnly = true)
    fun getAllUniversities(): List<University> {
        return universityRepository.findAll()
    }

    /**
     * Get a university by ID
     */
    @Transactional(readOnly = true)
    fun getUniversityById(id: UUID): University? {
        return universityRepository.findById(id).orElse(null)
    }

    /**
     * Get a university by name
     */
    @Transactional(readOnly = true)
    fun getUniversityByName(name: String): University? {
        return universityRepository.findByName(name)
    }

    /**
     * Save a university
     */
    @Transactional
    fun saveUniversity(university: University): University {
        return universityRepository.save(university)
    }

    /**
     * Delete a university by ID
     */
    @Transactional
    fun deleteUniversity(id: UUID) {
        universityRepository.deleteById(id)
    }
}