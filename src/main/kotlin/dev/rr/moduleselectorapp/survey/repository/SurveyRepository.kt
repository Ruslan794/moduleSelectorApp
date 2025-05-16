package dev.rr.moduleselectorapp.survey.repository

import dev.rr.moduleselectorapp.survey.model.SurveyData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SurveyRepository : JpaRepository<SurveyData, UUID> {

    fun findByEmail(email: String): List<SurveyData>

    fun findByNameAndSurname(name: String, surname: String): List<SurveyData>

    fun findByCountry(country: String): List<SurveyData>

    fun findByMatriculation(matriculation: String): SurveyData?
}