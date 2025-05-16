package dev.rr.moduleselectorapp.survey.service

import jakarta.servlet.http.HttpSession
import dev.rr.moduleselectorapp.survey.model.SurveyData
import dev.rr.moduleselectorapp.survey.repository.SurveyRepository
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
    private val httpSession: HttpSession
) {

    object SessionKeys {
        const val SURVEY_DATA = "SURVEY_DATA"
    }

    fun saveToSession(surveyData: SurveyData) {
        httpSession.setAttribute(SessionKeys.SURVEY_DATA, surveyData)
    }


    fun getFromSession(): SurveyData? {
        return httpSession.getAttribute(SessionKeys.SURVEY_DATA) as? SurveyData
    }

    fun clearSession() {
        httpSession.removeAttribute(SessionKeys.SURVEY_DATA)
    }


    fun persistSurveyData(): SurveyData? {
        val surveyData = getFromSession() ?: return null
        val savedData = surveyRepository.save(surveyData)
        clearSession()
        return savedData
    }

    fun updateSessionData(updatedData: SurveyData) {
        val existingData = getFromSession()
        if (existingData != null) {
            existingData.id?.let { updatedData.id = it }

            existingData.name = updatedData.name
            existingData.surname = updatedData.surname
            existingData.birthDate = updatedData.birthDate
            existingData.matriculation = updatedData.matriculation
            existingData.country = updatedData.country
            existingData.email = updatedData.email

            saveToSession(existingData)
        } else {
            saveToSession(updatedData)
        }
    }
}