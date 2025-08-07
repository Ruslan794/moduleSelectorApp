package dev.rr.moduleselectorapp.config

import dev.rr.moduleselectorapp.common.TempData
import dev.rr.moduleselectorapp.subject.model.*
import dev.rr.moduleselectorapp.subject.repository.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Configuration
class DataInitializationConfig(
    private val courseRepository: CourseRepository,
    private val universityRepository: UniversityRepository,
    private val optionalCourseGroupRepository: OptionalCourseGroupRepository,
    private val abroadSemesterRepository: AbroadSemesterRepository,
    private val subjectRepository: SubjectRepository
) {
    private val logger = LoggerFactory.getLogger(DataInitializationConfig::class.java)

    @Bean
    fun dataInitializer(): CommandLineRunner {
        return CommandLineRunner {
            initializeData()
        }
    }

    @Transactional
    fun initializeData() {
        // Check if data already exists
        if (subjectRepository.count() > 0) {
            logger.info("Database already initialized, skipping data initialization")
            return
        }

        logger.info("Starting database initialization...")

        // Step 1: Save all courses first (including optional course courses)
        val allCoursesMap = mutableMapOf<String, Course>()

        // Regular courses
        TempData.allCourses.forEach { course ->
            val savedCourse = courseRepository.save(Course().apply {
                name = course.name
                code = course.code
                semester = course.semester
            })
            allCoursesMap[course.code] = savedCourse
        }
        logger.info("Saved ${TempData.allCourses.size} regular courses")

        // Collect and save all courses from optional groups and abroad semesters
        val optionalAndAbroadCourses = mutableListOf<Course>()

        // CS optional groups
        TempData.computerScience.optionalCourseGroups.forEach { group ->
            group.coursesToChoose.forEach { course ->
                optionalAndAbroadCourses.add(course)
            }
        }

        // Business optional groups
        TempData.businessAdministration.optionalCourseGroups.forEach { group ->
            group.coursesToChoose.forEach { course ->
                optionalAndAbroadCourses.add(course)
            }
        }

        // CS abroad semesters
        TempData.computerScience.abroadSemestersToChoose.forEach { semester ->
            // Add compulsory courses
            semester.compulsoryCourses.forEach { course ->
                optionalAndAbroadCourses.add(course)
            }

            // Add courses from optional groups
            semester.optionalCourseGroups.forEach { group ->
                group.coursesToChoose.forEach { course ->
                    optionalAndAbroadCourses.add(course)
                }
            }
        }

        // Business abroad semesters
        TempData.businessAdministration.abroadSemestersToChoose.forEach { semester ->
            // Add compulsory courses
            semester.compulsoryCourses.forEach { course ->
                optionalAndAbroadCourses.add(course)
            }

            // Add courses from optional groups
            semester.optionalCourseGroups.forEach { group ->
                group.coursesToChoose.forEach { course ->
                    optionalAndAbroadCourses.add(course)
                }
            }
        }

        // Save all additional courses
        optionalAndAbroadCourses.forEach { course ->
            val savedCourse = courseRepository.save(Course().apply {
                name = course.name
                code = course.code
                semester = course.semester
            })
            allCoursesMap[course.code] = savedCourse
        }
        logger.info("Saved ${optionalAndAbroadCourses.size} additional courses")

        // Step 2: Save universities
        val universitiesMap = mutableMapOf<String, University>()
        TempData.allUniversities.forEach { university ->
            val savedUniversity = universityRepository.save(University().apply {
                name = university.name
                shortName = university.shortName
                country = university.country
            })
            universitiesMap[university.shortName] = savedUniversity
        }
        logger.info("Saved ${universitiesMap.size} universities")

        // Step 3: Create and save optional course groups
        val csOptionalGroups = mutableListOf<OptionalCourseGroup>()
        TempData.computerScience.optionalCourseGroups.forEach { group ->
            val newGroup = OptionalCourseGroup().apply {
                name = group.name
                semester = group.semester
            }

            val savedGroup = optionalCourseGroupRepository.save(newGroup)

            // Add courses to the group
            group.coursesToChoose.forEach { course ->
                val savedCourse = allCoursesMap[course.code]
                if (savedCourse != null) {
                    savedGroup.coursesToChoose.add(savedCourse)
                }
            }

            // Update the group with courses
            val updatedGroup = optionalCourseGroupRepository.save(savedGroup)
            csOptionalGroups.add(updatedGroup)
        }
        logger.info("Saved ${csOptionalGroups.size} Computer Science optional course groups")

        val businessOptionalGroups = mutableListOf<OptionalCourseGroup>()
        TempData.businessAdministration.optionalCourseGroups.forEach { group ->
            val newGroup = OptionalCourseGroup().apply {
                name = group.name
                semester = group.semester
            }

            val savedGroup = optionalCourseGroupRepository.save(newGroup)

            // Add courses to the group
            group.coursesToChoose.forEach { course ->
                val savedCourse = allCoursesMap[course.code]
                if (savedCourse != null) {
                    savedGroup.coursesToChoose.add(savedCourse)
                }
            }

            // Update the group with courses
            val updatedGroup = optionalCourseGroupRepository.save(savedGroup)
            businessOptionalGroups.add(updatedGroup)
        }
        logger.info("Saved ${businessOptionalGroups.size} Business Administration optional course groups")

        // Step 4: Create and save abroad semesters
        val csAbroadSemesters = mutableListOf<AbroadSemester>()
        TempData.computerScience.abroadSemestersToChoose.forEach { tempSemester ->
            val university = universitiesMap[tempSemester.university.shortName]
            if (university != null) {
                val newSemester = AbroadSemester().apply {
                    semester = tempSemester.semester
                    this.university = university
                }

                val savedSemester = abroadSemesterRepository.save(newSemester)

                // Add compulsory courses
                tempSemester.compulsoryCourses.forEach { course ->
                    val savedCourse = allCoursesMap[course.code]
                    if (savedCourse != null) {
                        savedSemester.compulsoryCourses.add(savedCourse)
                    }
                }

                // Create and add optional groups
                tempSemester.optionalCourseGroups.forEach { tempGroup ->
                    val newGroup = OptionalCourseGroup().apply {
                        name = tempGroup.name
                        semester = tempGroup.semester
                    }

                    val savedGroup = optionalCourseGroupRepository.save(newGroup)

                    // Add courses to the group
                    tempGroup.coursesToChoose.forEach { course ->
                        val savedCourse = allCoursesMap[course.code]
                        if (savedCourse != null) {
                            savedGroup.coursesToChoose.add(savedCourse)
                        }
                    }

                    // Update the group with courses
                    val updatedGroup = optionalCourseGroupRepository.save(savedGroup)
                    savedSemester.optionalCourseGroups.add(updatedGroup)
                }

                // Update the semester with all relations
                val updatedSemester = abroadSemesterRepository.save(savedSemester)
                csAbroadSemesters.add(updatedSemester)
            }
        }
        logger.info("Saved ${csAbroadSemesters.size} Computer Science abroad semesters")

        val businessAbroadSemesters = mutableListOf<AbroadSemester>()
        TempData.businessAdministration.abroadSemestersToChoose.forEach { tempSemester ->
            val university = universitiesMap[tempSemester.university.shortName]
            if (university != null) {
                val newSemester = AbroadSemester().apply {
                    semester = tempSemester.semester
                    this.university = university
                }

                val savedSemester = abroadSemesterRepository.save(newSemester)

                // Add compulsory courses
                tempSemester.compulsoryCourses.forEach { course ->
                    val savedCourse = allCoursesMap[course.code]
                    if (savedCourse != null) {
                        savedSemester.compulsoryCourses.add(savedCourse)
                    }
                }

                // Create and add optional groups
                tempSemester.optionalCourseGroups.forEach { tempGroup ->
                    val newGroup = OptionalCourseGroup().apply {
                        name = tempGroup.name
                        semester = tempGroup.semester
                    }

                    val savedGroup = optionalCourseGroupRepository.save(newGroup)

                    // Add courses to the group
                    tempGroup.coursesToChoose.forEach { course ->
                        val savedCourse = allCoursesMap[course.code]
                        if (savedCourse != null) {
                            savedGroup.coursesToChoose.add(savedCourse)
                        }
                    }

                    // Update the group with courses
                    val updatedGroup = optionalCourseGroupRepository.save(savedGroup)
                    savedSemester.optionalCourseGroups.add(updatedGroup)
                }

                // Update the semester with all relations
                val updatedSemester = abroadSemesterRepository.save(savedSemester)
                businessAbroadSemesters.add(updatedSemester)
            }
        }
        logger.info("Saved ${businessAbroadSemesters.size} Business Administration abroad semesters")

        // Step 5: Create and save subjects
        // Computer Science subject
        val csSubject = Subject().apply {
            name = TempData.computerScience.name
        }
        val savedCsSubject = subjectRepository.save(csSubject)

        // Add compulsory courses
        TempData.computerScience.compulsoryCourses.forEach { course ->
            val savedCourse = allCoursesMap[course.code]
            if (savedCourse != null) {
                savedCsSubject.compulsoryCourses.add(savedCourse)
            }
        }

        // Add optional course groups
        savedCsSubject.optionalCourseGroups.addAll(csOptionalGroups)

        // Add abroad semesters
        savedCsSubject.abroadSemestersToChoose.addAll(csAbroadSemesters)

        // Update subject with all relations
        subjectRepository.save(savedCsSubject)
        logger.info("Saved Computer Science subject with ${savedCsSubject.compulsoryCourses.size} compulsory courses")

        // Business Administration subject
        val businessSubject = Subject().apply {
            name = TempData.businessAdministration.name
        }
        val savedBusinessSubject = subjectRepository.save(businessSubject)

        // Add compulsory courses
        TempData.businessAdministration.compulsoryCourses.forEach { course ->
            val savedCourse = allCoursesMap[course.code]
            if (savedCourse != null) {
                savedBusinessSubject.compulsoryCourses.add(savedCourse)
            }
        }

        // Add optional course groups
        savedBusinessSubject.optionalCourseGroups.addAll(businessOptionalGroups)

        // Add abroad semesters
        savedBusinessSubject.abroadSemestersToChoose.addAll(businessAbroadSemesters)

        // Update subject with all relations
        subjectRepository.save(savedBusinessSubject)
        logger.info("Saved Business Administration subject with ${savedBusinessSubject.compulsoryCourses.size} compulsory courses")

        logger.info("Database initialization completed successfully")
    }
}