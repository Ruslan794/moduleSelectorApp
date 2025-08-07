package dev.rr.moduleselectorapp.subject.service

import dev.rr.moduleselectorapp.subject.model.AbroadSemester
import dev.rr.moduleselectorapp.subject.repository.AbroadSemesterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AbroadSemesterService(
    private val abroadSemesterRepository: AbroadSemesterRepository
) {

    /**
     * Get all abroad semesters
     */
    @Transactional(readOnly = true)
    fun getAllAbroadSemesters(): List<AbroadSemester> {
        return abroadSemesterRepository.findAll()
    }

    /**
     * Get abroad semesters by semester number
     */
    @Transactional(readOnly = true)
    fun getAbroadSemestersBySemester(semester: Int): List<AbroadSemester> {
        return abroadSemesterRepository.findBySemester(semester)
    }

    /**
     * Get an abroad semester by ID
     */
    @Transactional(readOnly = true)
    fun getAbroadSemesterById(id: UUID): AbroadSemester? {
        val abroadSemester = abroadSemesterRepository.findById(id).orElse(null) ?: return null

        // Initialize the lazy collections
        abroadSemester.compulsoryCourses.size
        abroadSemester.optionalCourseGroups.forEach { group ->
            group.coursesToChoose.size
        }

        return abroadSemester
    }

    /**
     * Get abroad semesters by university ID
     */
    @Transactional(readOnly = true)
    fun getAbroadSemestersByUniversity(universityId: UUID): List<AbroadSemester> {
        return abroadSemesterRepository.findByUniversityId(universityId)
    }

    /**
     * Save an abroad semester
     */
    @Transactional
    fun saveAbroadSemester(abroadSemester: AbroadSemester): AbroadSemester {
        return abroadSemesterRepository.save(abroadSemester)
    }

    /**
     * Delete an abroad semester by ID
     */
    @Transactional
    fun deleteAbroadSemester(id: UUID) {
        abroadSemesterRepository.deleteById(id)
    }
}