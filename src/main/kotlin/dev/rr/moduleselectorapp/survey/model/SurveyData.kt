package dev.rr.moduleselectorapp.survey.model

import dev.rr.moduleselectorapp.common.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
@Table(name = "survey_data")
class SurveyData : BaseEntity() {

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, message = "Name must be at least 2 characters")
    var name: String = ""

    @field:NotBlank(message = "Surname is required")
    @field:Size(min = 2, message = "Surname must be at least 2 characters")
    var surname: String = ""

    @field:NotBlank(message = "Birth date is required")
    @field:Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4}", message = "Birth date must be in format dd.MM.yyyy")
    var birthDate: String = ""

    var matriculation: String = ""

    @field:NotBlank(message = "Country is required")
    var country: String = ""

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please enter a valid email address")
    var email: String = ""

    override fun toString(): String {
        return "SurveyData(id=$id, name='$name', surname='$surname', birthDate='$birthDate', matriculation='$matriculation', country='$country', email='$email')"
    }
}