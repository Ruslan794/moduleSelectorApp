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

    @Transactional(readOnly = true)
    fun getAllOptionalGroups(): List<OptionalCourseGroup> {
        return optionalCourseGroupRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getOptionalGroupsBySemester(semester: Int): List<OptionalCourseGroup> {
        return optionalCourseGroupRepository.findBySemester(semester)
    }

    @Transactional(readOnly = true)
    fun getOptionalGroupById(id: UUID): OptionalCourseGroup? {
        val group = optionalCourseGroupRepository.findById(id).orElse(null) ?: return null

        group.coursesToChoose.size

        return group
    }

    @Transactional
    fun saveOptionalGroup(group: OptionalCourseGroup): OptionalCourseGroup {
        return optionalCourseGroupRepository.save(group)
    }

    @Transactional
    fun deleteOptionalGroup(id: UUID) {
        optionalCourseGroupRepository.deleteById(id)
    }
}