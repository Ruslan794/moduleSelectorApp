package dev.rr.moduleselectorapp.selector

import dev.rr.moduleselectorapp.selector.service.StudentSelectionService
import dev.rr.moduleselectorapp.subject.model.Course
import dev.rr.moduleselectorapp.subject.service.SubjectService
import dev.rr.moduleselectorapp.survey.service.SurveyService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/selector")
class SelectorController(
    private val surveyService: SurveyService,
    private val subjectService: SubjectService,
    private val studentSelectionService: StudentSelectionService
) {
    private val logger = LoggerFactory.getLogger(SelectorController::class.java)

    @GetMapping
    fun showSubjectSelection(model: Model, redirectAttributes: RedirectAttributes): String {
        val surveyData = surveyService.getFromSession()
        if (surveyData == null) {
            redirectAttributes.addFlashAttribute("error", "Please complete the survey first")
            return "redirect:/survey"
        }

        val selection = studentSelectionService.initializeFromSession()
        if (selection == null) {
            redirectAttributes.addFlashAttribute("error", "Unable to initialize selection")
            return "redirect:/survey"
        }

        val subjects = subjectService.getAllSubjects()
        model.addAttribute("subjects", subjects)
        model.addAttribute("selection", selection)
        model.addAttribute("surveyData", surveyData)

        return "subject-selection"
    }

    @PostMapping("/subject")
    fun selectSubject(
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val selection = studentSelectionService.selectSubject(subjectId)
        if (selection != null) {
            return "redirect:/selector/courses"
        }

        redirectAttributes.addFlashAttribute("error", "Failed to select subject")
        return "redirect:/selector"
    }

    @GetMapping("/courses")
    fun showCourseSelection(model: Model, redirectAttributes: RedirectAttributes): String {
        val selection = studentSelectionService.getFromSession()
        if (selection?.selectedSubject == null) {
            redirectAttributes.addFlashAttribute("error", "Please select a subject first")
            return "redirect:/selector"
        }

        val subject = selection.selectedSubject!!

        val compulsoryCoursesBySemester = subject.getCompulsoryCourses()
            .groupBy { it.semester }
            .toSortedMap()

        val optionalGroupsBySemester = subject.optionalCourseGroups
            .groupBy { it.semester }
            .toSortedMap()

        val abroadSemestersBySemester = subject.abroadSemestersToChoose
            .groupBy { it.semester }
            .toSortedMap()

        model.addAttribute("selection", selection)
        model.addAttribute("subject", subject)
        model.addAttribute("compulsoryCoursesBySemester", compulsoryCoursesBySemester)
        model.addAttribute("optionalGroupsBySemester", optionalGroupsBySemester)
        model.addAttribute("abroadSemestersBySemester", abroadSemestersBySemester)

        return "course-selection"
    }

    @PostMapping("/optional-course")
    @ResponseBody
    fun selectOptionalCourse(
        @RequestParam courseId: UUID,
        @RequestParam groupId: UUID
    ): ResponseEntity<String> {
        studentSelectionService.selectOptionalCourse(courseId)
        return ResponseEntity.ok("Success")
    }

    @PostMapping("/abroad-semester")
    @ResponseBody
    fun selectAbroadSemester(@RequestParam abroadSemesterId: UUID): ResponseEntity<String> {
        studentSelectionService.selectAbroadSemester(abroadSemesterId)
        return ResponseEntity.ok("Success")
    }

    @PostMapping("/abroad-optional-course")
    @ResponseBody
    fun selectAbroadOptionalCourse(
        @RequestParam courseId: UUID,
        @RequestParam groupId: UUID
    ): ResponseEntity<String> {
        studentSelectionService.selectAbroadOptionalCourse(courseId)
        return ResponseEntity.ok("Success")
    }

    @GetMapping("/review")
    fun showReview(model: Model, redirectAttributes: RedirectAttributes): String {
        val selection = studentSelectionService.getFromSession()
        if (selection?.selectedSubject == null) {
            redirectAttributes.addFlashAttribute("error", "Please complete your selection first")
            return "redirect:/selector"
        }

        val subject = subjectService.getSubjectById(selection.selectedSubject!!.id!!)!!
        val compulsoryCourses = subject.getCompulsoryCourses()

        val selectedOptionalCoursesByGroup = mutableMapOf<String, List<Course>>()
        val selectedAbroadOptionalCourses = mutableListOf<Course>()

        selection.selectedOptionalCourses.forEach { course ->
            when {
                course.optionalGroup != null && course.abroadSemester == null -> {
                    val groupKey = "Optional Group - Semester ${course.semester}"
                    selectedOptionalCoursesByGroup[groupKey] =
                        (selectedOptionalCoursesByGroup[groupKey] ?: emptyList()) + course
                }
                course.abroadSemester != null -> {
                    selectedAbroadOptionalCourses.add(course)
                }
            }
        }

        model.addAttribute("selection", selection)
        model.addAttribute("subject", subject)
        model.addAttribute("compulsoryCourses", compulsoryCourses)
        model.addAttribute("selectedOptionalCoursesByGroup", selectedOptionalCoursesByGroup)
        model.addAttribute("selectedAbroadOptionalCourses", selectedAbroadOptionalCourses)

        return "selection-review"
    }

    @PostMapping("/finalize")
    fun finalizeSelection(redirectAttributes: RedirectAttributes): String {
        val finalizedSelection = studentSelectionService.finalizeSelection()

        if (finalizedSelection != null) {
            redirectAttributes.addFlashAttribute("selection", finalizedSelection)
            return "redirect:/selector/complete"
        }

        redirectAttributes.addFlashAttribute("error", "Failed to finalize selection")
        return "redirect:/selector/review"
    }

    @GetMapping("/complete")
    fun showCompletion(model: Model, @ModelAttribute("selection") selection: Any?): String {
        if (selection != null) {
            model.addAttribute("selection", selection)
            return "selection-complete"
        }
        return "redirect:/"
    }
}