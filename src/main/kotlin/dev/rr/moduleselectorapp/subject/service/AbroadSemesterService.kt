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

    @Transactional(readOnly = true)
    fun getAllAbroadSemesters(): List<AbroadSemester> {
        return abroadSemesterRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getAbroadSemestersBySemester(semester: Int): List<AbroadSemester> {
        return abroadSemesterRepository.findBySemester(semester)
    }

    @Transactional(readOnly = true)
    fun getAbroadSemesterById(id: UUID): AbroadSemester? {
        val abroadSemester = abroadSemesterRepository.findById(id).orElse(null) ?: return null

        abroadSemester.courses.size
        abroadSemester.optionalCourseGroups.forEach { group ->
            group.coursesToChoose.size
        }

        return abroadSemester
    }

    @Transactional(readOnly = true)
    fun getAbroadSemestersByUniversity(universityId: UUID): List<AbroadSemester> {
        return abroadSemesterRepository.findByUniversityId(universityId)
    }

    @Transactional
    fun saveAbroadSemester(abroadSemester: AbroadSemester): AbroadSemester {
        return abroadSemesterRepository.save(abroadSemester)
    }

    @Transactional
    fun deleteAbroadSemester(id: UUID) {
        abroadSemesterRepository.deleteById(id)
    }
}