package dev.rr.moduleselectorapp.subject.service

import dev.rr.moduleselectorapp.subject.model.OptionalCourseGroup
import dev.rr.moduleselectorapp.subject.repository.OptionalCourseGroupRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OptionalCourseGroupService(
    private val optionalCourseGroupRepository: OptionalCourseGroupRepository
) {

    /**
     * Get all optional course groups
     */
    @Transactional(readOnly = true)
    fun getAllOptionalGroups(): List<OptionalCourseGroup> {
        return optionalCourseGroupRepository.findAll()
    }

    /**
     * Get optional groups by semester
     */
    @Transactional(readOnly = true)
    fun getOptionalGroupsBySemester(semester: Int): List<OptionalCourseGroup> {
        return optionalCourseGroupRepository.findBySemester(semester)
    }

    /**
     * Get an optional group by ID
     */
    @Transactional(readOnly = true)
    fun getOptionalGroupById(id: UUID): OptionalCourseGroup? {
        val group = optionalCourseGroupRepository.findById(id).orElse(null) ?: return null

        // Initialize the lazy collections
        group.coursesToChoose.size

        return group
    }

    /**
     * Save an optional course group
     */
    @Transactional
    fun saveOptionalGroup(group: OptionalCourseGroup): OptionalCourseGroup {
        return optionalCourseGroupRepository.save(group)
    }

    /**
     * Delete an optional course group by ID
     */
    @Transactional
    fun deleteOptionalGroup(id: UUID) {
        optionalCourseGroupRepository.deleteById(id)
    }
}