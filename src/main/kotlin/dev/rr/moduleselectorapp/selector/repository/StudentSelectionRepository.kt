package dev.rr.moduleselectorapp.selector.repository

import dev.rr.moduleselectorapp.selector.model.StudentSelection
import dev.rr.moduleselectorapp.survey.model.SurveyData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentSelectionRepository : JpaRepository<StudentSelection, UUID> {

    fun findBySurveyData(surveyData: SurveyData): StudentSelection?

    fun findBySurveyDataId(surveyDataId: UUID): StudentSelection?

    fun findByIsCompletedTrue(): List<StudentSelection>

    fun findBySelectedSubjectId(subjectId: UUID): List<StudentSelection>
}