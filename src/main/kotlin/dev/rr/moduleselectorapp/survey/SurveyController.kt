package dev.rr.moduleselectorapp.survey

import dev.rr.moduleselectorapp.survey.service.SurveyService
import jakarta.validation.Valid
import dev.rr.moduleselectorapp.survey.model.SurveyData
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping
class SurveyController(
    private val surveyService: SurveyService
) {
    private val logger = LoggerFactory.getLogger(SurveyController::class.java)

    @GetMapping("/")
    fun rootPath(model: Model): String {
        val sessionData = surveyService.getFromSession()

        if (sessionData != null) {
            return "redirect:/selector"
        }

        model.addAttribute("surveyData", SurveyData())
        return "survey_form"
    }

    @GetMapping("/survey")
    fun showSurveyForm(model: Model): String {
        val sessionData = surveyService.getFromSession()

        model.addAttribute("surveyData", sessionData ?: SurveyData())
        return "survey_form"
    }

    @PostMapping("/survey")
    fun processSurveyForm(
        @Valid @ModelAttribute("surveyData") surveyData: SurveyData,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            return "survey_form"
        }

        surveyService.updateSessionData(surveyData)

        val savedData = surveyService.getFromSession()
        logger.info("Survey data saved in session: $savedData")
        logger.info("Survey data: ${savedData.toString()}")

        redirectAttributes.addFlashAttribute("success", "Thanks!")
        return "redirect:/"
    }

    @GetMapping("/survey/clear")
    fun clearForm(redirectAttributes: RedirectAttributes): String {
        surveyService.clearSession()
        redirectAttributes.addFlashAttribute("surveyData", SurveyData())
        return "redirect:/survey"
    }

}