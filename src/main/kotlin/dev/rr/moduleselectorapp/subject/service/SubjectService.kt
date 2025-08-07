package dev.rr.moduleselectorapp.subject.service

import dev.rr.moduleselectorapp.subject.model.Subject
import dev.rr.moduleselectorapp.subject.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository
) {


    @Transactional(readOnly = true)
    fun getAllSubjects(): List<Subject> {
        val subjects = subjectRepository.findAll()

        subjects.forEach { subject ->
            subject.compulsoryCourses.size
            subject.optionalCourseGroups.forEach { group ->
                group.coursesToChoose.size
            }
            subject.abroadSemestersToChoose.forEach { abroad ->
                abroad.compulsoryCourses.size
                abroad.optionalCourseGroups.forEach { group ->
                    group.coursesToChoose.size
                }
                abroad.university.name
            }
        }

        return subjects
    }


    @Transactional(readOnly = true)
    fun getSubjectById(id: UUID): Subject? {
        val subject = subjectRepository.findById(id).orElse(null) ?: return null

        // Initialize all lazy collections
        subject.compulsoryCourses.size
        subject.optionalCourseGroups.forEach { group ->
            group.coursesToChoose.size
        }
        subject.abroadSemestersToChoose.forEach { abroad ->
            abroad.compulsoryCourses.size
            abroad.optionalCourseGroups.forEach { group ->
                group.coursesToChoose.size
            }
            abroad.university.name
        }

        return subject
    }


    @Transactional
    fun saveSubject(subject: Subject): Subject {
        return subjectRepository.save(subject)
    }


    @Transactional
    fun deleteSubject(id: UUID) {
        subjectRepository.deleteById(id)
    }
}