package dev.rr.moduleselectorapp.admin.controller

import dev.rr.moduleselectorapp.subject.model.*
import dev.rr.moduleselectorapp.subject.service.CourseService
import dev.rr.moduleselectorapp.subject.service.SubjectService
import dev.rr.moduleselectorapp.subject.service.UniversityService
import dev.rr.moduleselectorapp.subject.service.OptionalCourseGroupService
import dev.rr.moduleselectorapp.subject.service.AbroadSemesterService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/secured")
class AdminController(
    private val subjectService: SubjectService,
    private val courseService: CourseService,
    private val universityService: UniversityService,
    private val optionalGroupService: OptionalCourseGroupService,
    private val abroadSemesterService: AbroadSemesterService
) {

    @GetMapping("/", "/dashboard")
    fun showDashboard(): String {
        return "dashboard"
    }

    @GetMapping("/subjects")
    fun showSubjects(model: Model): String {
        val subjects = subjectService.getAllSubjects()
        model.addAttribute("subjects", subjects)
        model.addAttribute("totalSubjects", subjects.size)

        // Calculate total courses across all subjects for display
        val totalCourses = subjects.sumOf { subject ->
            subject.compulsoryCourses.size +
                    subject.optionalCourseGroups.sumOf { it.coursesToChoose.size } +
                    subject.abroadSemestersToChoose.sumOf { abroad ->
                        abroad.compulsoryCourses.size +
                                abroad.optionalCourseGroups.sumOf { it.coursesToChoose.size }
                    }
        }
        model.addAttribute("totalCourses", totalCourses)

        return "subjects"
    }

    @GetMapping("/subjects/{id}")
    fun showSubjectDetail(@PathVariable id: UUID, model: Model): String {
        val subject = subjectService.getSubjectById(id)
        if (subject != null) {
            // Add the subject to the model
            model.addAttribute("subject", subject)

            // Group compulsory courses by semester
            val compulsoryCoursesBySemester = subject.compulsoryCourses
                .groupBy { it.semester }
                .toSortedMap()
            model.addAttribute("compulsoryCoursesBySemester", compulsoryCoursesBySemester)

            // Group optional course groups by semester
            val optionalGroupsBySemester = subject.optionalCourseGroups
                .groupBy { it.semester }
                .toSortedMap()
            model.addAttribute("optionalGroupsBySemester", optionalGroupsBySemester)

            // Group abroad semesters by semester
            val abroadSemestersBySemester = subject.abroadSemestersToChoose
                .groupBy { it.semester }
                .toSortedMap()
            model.addAttribute("abroadSemestersBySemester", abroadSemestersBySemester)

            // Add all available courses for the dropdown
            model.addAttribute("allCourses", courseService.getAllCourses())

            // Add all universities for the dropdown
            model.addAttribute("universities", universityService.getAllUniversities())

            return "subject-detail"
        }
        return "redirect:/secured/subjects"
    }

    @GetMapping("/data")
    fun showStudentData(): String {
        return "data"
    }

    // Subject CRUD operations

    @GetMapping("/subjects/add")
    fun showAddSubjectForm(model: Model): String {
        model.addAttribute("subject", Subject())
        return "subject-form"
    }

    @PostMapping("/subjects/add")
    fun addSubject(@ModelAttribute subject: Subject, redirectAttributes: RedirectAttributes): String {
        val savedSubject = subjectService.saveSubject(subject)
        redirectAttributes.addFlashAttribute("success", "Subject '${subject.name}' created successfully")
        return "redirect:/secured/subjects/${savedSubject.id}"
    }

    @GetMapping("/subjects/edit/{id}")
    fun showEditSubjectForm(@PathVariable id: UUID, model: Model): String {
        val subject = subjectService.getSubjectById(id)
        if (subject != null) {
            model.addAttribute("subject", subject)
            return "subject-form"
        }
        return "redirect:/secured/subjects"
    }

    @PostMapping("/subjects/update-name/{id}")
    fun updateSubjectName(@PathVariable id: UUID, @RequestParam name: String, redirectAttributes: RedirectAttributes): String {
        val subject = subjectService.getSubjectById(id)
        if (subject != null) {
            subject.name = name
            subjectService.saveSubject(subject)
            redirectAttributes.addFlashAttribute("success", "Subject name updated successfully")
        }
        return "redirect:/secured/subjects/${id}"
    }

    @GetMapping("/subjects/delete/{id}")
    fun deleteSubject(@PathVariable id: UUID, redirectAttributes: RedirectAttributes): String {
        subjectService.deleteSubject(id)
        redirectAttributes.addFlashAttribute("success", "Subject deleted successfully")
        return "redirect:/secured/subjects"
    }

    // Course operations

    @PostMapping("/subjects/{id}/courses/add")
    fun addCourseToSubject(
        @PathVariable id: UUID,
        @RequestParam(required = false) courseId: UUID?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) code: String?,
        @RequestParam(required = false) semester: Int?,
        @RequestParam isNewCourse: Boolean,
        redirectAttributes: RedirectAttributes
    ): String {
        val subject = subjectService.getSubjectById(id) ?: return "redirect:/secured/subjects"

        val course = if (isNewCourse && name != null && code != null && semester != null) {
            // Create new course
            val newCourse = Course().apply {
                this.name = name
                this.code = code
                this.semester = semester
            }
            courseService.saveCourse(newCourse)
        } else if (!isNewCourse && courseId != null) {
            // Use existing course
            courseService.getCourseById(courseId)
        } else {
            null
        }

        if (course != null) {
            subject.compulsoryCourses.add(course)
            subjectService.saveSubject(subject)
            redirectAttributes.addFlashAttribute("success", "Course '${course.name}' added successfully")
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to add course")
        }

        return "redirect:/secured/subjects/${id}"
    }

    @GetMapping("/courses/edit/{id}")
    fun showEditCourseForm(@PathVariable id: UUID, model: Model, @RequestParam subjectId: UUID): String {
        val course = courseService.getCourseById(id)
        if (course != null) {
            model.addAttribute("course", course)
            model.addAttribute("subjectId", subjectId)
            return "course-form"
        }
        return "redirect:/secured/subjects/${subjectId}"
    }

    @PostMapping("/courses/update/{id}")
    fun updateCourse(
        @PathVariable id: UUID,
        @ModelAttribute course: Course,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val existingCourse = courseService.getCourseById(id)
        if (existingCourse != null) {
            existingCourse.name = course.name
            existingCourse.code = course.code
            existingCourse.semester = course.semester
            courseService.saveCourse(existingCourse)
            redirectAttributes.addFlashAttribute("success", "Course updated successfully")
        }

        return "redirect:/secured/subjects/${subjectId}"
    }

    @GetMapping("/subjects/{subjectId}/courses/remove/{courseId}")
    fun removeCourseFromSubject(
        @PathVariable subjectId: UUID,
        @PathVariable courseId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val subject = subjectService.getSubjectById(subjectId)
        val course = courseService.getCourseById(courseId)

        if (subject != null && course != null) {
            subject.compulsoryCourses.removeIf { it.id == courseId }
            subjectService.saveSubject(subject)
            redirectAttributes.addFlashAttribute("success", "Course '${course.name}' removed from ${subject.name}")
        }

        return "redirect:/secured/subjects/${subjectId}"
    }

    // Optional Course Group operations

    @PostMapping("/subjects/{id}/optional-groups/add")
    fun addOptionalGroupToSubject(
        @PathVariable id: UUID,
        @RequestParam name: String,
        @RequestParam semester: Int,
        redirectAttributes: RedirectAttributes
    ): String {
        val subject = subjectService.getSubjectById(id) ?: return "redirect:/secured/subjects"

        val group = OptionalCourseGroup().apply {
            this.name = name
            this.semester = semester
        }

        subject.optionalCourseGroups.add(group)
        subjectService.saveSubject(subject)
        redirectAttributes.addFlashAttribute("success", "Optional course group '$name' added successfully")

        return "redirect:/secured/subjects/${id}"
    }

    @GetMapping("/optional-groups/edit/{id}")
    fun showEditOptionalGroupForm(@PathVariable id: UUID, model: Model, @RequestParam subjectId: UUID): String {
        val group = optionalGroupService.getOptionalGroupById(id)
        if (group != null) {
            model.addAttribute("group", group)
            model.addAttribute("allCourses", courseService.getAllCourses())
            model.addAttribute("subjectId", subjectId)
            return "optional-group-form"
        }
        return "redirect:/secured/subjects/${subjectId}"
    }

    @PostMapping("/optional-groups/{groupId}/add-course")
    fun addCourseToOptionalGroup(
        @PathVariable groupId: UUID,
        @RequestParam courseId: UUID,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val group = optionalGroupService.getOptionalGroupById(groupId)
        val course = courseService.getCourseById(courseId)

        if (group != null && course != null) {
            if (!group.coursesToChoose.contains(course)) {
                group.coursesToChoose.add(course)
                optionalGroupService.saveOptionalGroup(group)
                redirectAttributes.addFlashAttribute("success", "Course '${course.name}' added to group")
            }
        }

        return "redirect:/secured/optional-groups/edit/${groupId}?subjectId=${subjectId}"
    }

    @GetMapping("/optional-groups/{groupId}/remove-course/{courseId}")
    fun removeCourseFromOptionalGroup(
        @PathVariable groupId: UUID,
        @PathVariable courseId: UUID,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val group = optionalGroupService.getOptionalGroupById(groupId)

        if (group != null) {
            group.coursesToChoose.removeIf { it.id == courseId }
            optionalGroupService.saveOptionalGroup(group)
            redirectAttributes.addFlashAttribute("success", "Course removed from group")
        }

        return "redirect:/secured/optional-groups/edit/${groupId}?subjectId=${subjectId}"
    }

    @GetMapping("/subjects/{subjectId}/optional-groups/remove/{groupId}")
    fun removeOptionalGroupFromSubject(
        @PathVariable subjectId: UUID,
        @PathVariable groupId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val subject = subjectService.getSubjectById(subjectId)

        if (subject != null) {
            subject.optionalCourseGroups.removeIf { it.id == groupId }
            subjectService.saveSubject(subject)
            redirectAttributes.addFlashAttribute("success", "Optional course group removed successfully")
        }

        return "redirect:/secured/subjects/${subjectId}"
    }

    // Abroad Semester operations

    @PostMapping("/subjects/{id}/abroad-semesters/add")
    fun addAbroadSemesterToSubject(
        @PathVariable id: UUID,
        @RequestParam semester: Int,
        @RequestParam(required = false) universityId: UUID?,
        @RequestParam(required = false) universityName: String?,
        @RequestParam(required = false) universityShortName: String?,
        @RequestParam(required = false) universityCountry: String?,
        @RequestParam isNewUniversity: Boolean,
        redirectAttributes: RedirectAttributes
    ): String {
        val subject = subjectService.getSubjectById(id) ?: return "redirect:/secured/subjects"

        val university = if (isNewUniversity && universityName != null && universityShortName != null && universityCountry != null) {
            // Create new university
            val newUniversity = University().apply {
                this.name = universityName
                this.shortName = universityShortName
                this.country = universityCountry
            }
            universityService.saveUniversity(newUniversity)
        } else if (!isNewUniversity && universityId != null) {
            // Use existing university
            universityService.getUniversityById(universityId)
        } else {
            null
        }

        if (university != null) {
            val abroadSemester = AbroadSemester().apply {
                this.semester = semester
                this.university = university
            }

            subject.abroadSemestersToChoose.add(abroadSemester)
            subjectService.saveSubject(subject)
            redirectAttributes.addFlashAttribute("success", "Abroad semester at ${university.name} added successfully")
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to add abroad semester")
        }

        return "redirect:/secured/subjects/${id}"
    }

    @GetMapping("/abroad-semesters/edit/{id}")
    fun showEditAbroadSemesterForm(@PathVariable id: UUID, model: Model, @RequestParam subjectId: UUID): String {
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(id)
        if (abroadSemester != null) {
            model.addAttribute("abroadSemester", abroadSemester)
            model.addAttribute("allCourses", courseService.getAllCourses())
            model.addAttribute("universities", universityService.getAllUniversities())
            model.addAttribute("subjectId", subjectId)
            return "abroad-semester-form"
        }
        return "redirect:/secured/subjects/${subjectId}"
    }

    @PostMapping("/abroad-semesters/{abroadId}/add-compulsory-course")
    fun addCompulsoryCourseToAbroadSemester(
        @PathVariable abroadId: UUID,
        @RequestParam courseId: UUID,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(abroadId)
        val course = courseService.getCourseById(courseId)

        if (abroadSemester != null && course != null) {
            if (!abroadSemester.compulsoryCourses.contains(course)) {
                abroadSemester.compulsoryCourses.add(course)
                abroadSemesterService.saveAbroadSemester(abroadSemester)
                redirectAttributes.addFlashAttribute("success", "Course '${course.name}' added to abroad semester")
            }
        }

        return "redirect:/secured/abroad-semesters/edit/${abroadId}?subjectId=${subjectId}"
    }

    @GetMapping("/abroad-semesters/{abroadId}/remove-compulsory-course/{courseId}")
    fun removeCompulsoryCourseFromAbroadSemester(
        @PathVariable abroadId: UUID,
        @PathVariable courseId: UUID,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(abroadId)

        if (abroadSemester != null) {
            abroadSemester.compulsoryCourses.removeIf { it.id == courseId }
            abroadSemesterService.saveAbroadSemester(abroadSemester)
            redirectAttributes.addFlashAttribute("success", "Course removed from abroad semester")
        }

        return "redirect:/secured/abroad-semesters/edit/${abroadId}?subjectId=${subjectId}"
    }

    @PostMapping("/abroad-semesters/{abroadId}/add-optional-group")
    fun addOptionalGroupToAbroadSemester(
        @PathVariable abroadId: UUID,
        @RequestParam name: String,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(abroadId)

        if (abroadSemester != null) {
            val group = OptionalCourseGroup().apply {
                this.name = name
                this.semester = abroadSemester.semester
            }

            abroadSemester.optionalCourseGroups.add(group)
            abroadSemesterService.saveAbroadSemester(abroadSemester)
            redirectAttributes.addFlashAttribute("success", "Optional course group '$name' added to abroad semester")
        }

        return "redirect:/secured/abroad-semesters/edit/${abroadId}?subjectId=${subjectId}"
    }

    @GetMapping("/subjects/{subjectId}/abroad-semesters/remove/{abroadId}")
    fun removeAbroadSemesterFromSubject(
        @PathVariable subjectId: UUID,
        @PathVariable abroadId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val subject = subjectService.getSubjectById(subjectId)

        if (subject != null) {
            subject.abroadSemestersToChoose.removeIf { it.id == abroadId }
            subjectService.saveSubject(subject)
            redirectAttributes.addFlashAttribute("success", "Abroad semester removed successfully")
        }

        return "redirect:/secured/subjects/${subjectId}"
    }

    @PostMapping("/abroad-semesters/{id}/change-university")
    fun changeAbroadSemesterUniversity(
        @PathVariable id: UUID,
        @RequestParam(required = false) universityId: UUID?,
        @RequestParam(required = false) universityName: String?,
        @RequestParam(required = false) universityShortName: String?,
        @RequestParam(required = false) universityCountry: String?,
        @RequestParam isNewUniversity: Boolean,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(id)

        if (abroadSemester != null) {
            val university = if (isNewUniversity && universityName != null && universityShortName != null && universityCountry != null) {
                // Create new university
                val newUniversity = University().apply {
                    this.name = universityName
                    this.shortName = universityShortName
                    this.country = universityCountry
                }
                universityService.saveUniversity(newUniversity)
            } else if (!isNewUniversity && universityId != null) {
                // Use existing university
                universityService.getUniversityById(universityId)
            } else {
                null
            }

            if (university != null) {
                abroadSemester.university = university
                abroadSemesterService.saveAbroadSemester(abroadSemester)
                redirectAttributes.addFlashAttribute("success", "University changed successfully")
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to change university")
            }
        }

        return "redirect:/secured/abroad-semesters/edit/${id}?subjectId=${subjectId}"
    }

    @GetMapping("/abroad-semesters/{abroadId}/remove-optional-group/{groupId}")
    fun removeOptionalGroupFromAbroadSemester(
        @PathVariable abroadId: UUID,
        @PathVariable groupId: UUID,
        @RequestParam subjectId: UUID,
        redirectAttributes: RedirectAttributes
    ): String {
        val abroadSemester = abroadSemesterService.getAbroadSemesterById(abroadId)

        if (abroadSemester != null) {
            abroadSemester.optionalCourseGroups.removeIf { it.id == groupId }
            abroadSemesterService.saveAbroadSemester(abroadSemester)
            redirectAttributes.addFlashAttribute("success", "Optional group removed from abroad semester")
        }

        return "redirect:/secured/abroad-semesters/edit/${abroadId}?subjectId=${subjectId}"
    }
}