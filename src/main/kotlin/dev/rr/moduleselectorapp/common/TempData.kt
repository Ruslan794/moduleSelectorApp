package dev.rr.moduleselectorapp.common

import dev.rr.moduleselectorapp.subject.model.AbroadSemester
import dev.rr.moduleselectorapp.subject.model.Course
import dev.rr.moduleselectorapp.subject.model.OptionalCourseGroup
import dev.rr.moduleselectorapp.subject.model.Subject
import dev.rr.moduleselectorapp.subject.model.University
import dev.rr.moduleselectorapp.survey.model.SurveyData
import java.util.*
import kotlin.collections.get


object TempData {

    private val programmingBasics = Course().apply {
        name = "Programming Basics"
        code = "CS101"
        semester = 1
    }

    private val dataStructures = Course().apply {
        name = "Data Structures"
        code = "CS102"
        semester = 2
    }

    private val algorithms = Course().apply {
        name = "Algorithms"
        code = "CS201"
        semester = 3
    }

    private val databaseSystems = Course().apply {
        name = "Database Systems"
        code = "CS202"
        semester = 2
    }

    private val webDevelopment = Course().apply {
        name = "Web Development"
        code = "CS301"
        semester = 3
    }

    private val artificialIntelligence = Course().apply {
        name = "Artificial Intelligence"
        code = "CS302"
        semester = 3
    }

    private val machineLearning = Course().apply {
        name = "Machine Learning"
        code = "CS401"
        semester = 4
    }

    private val dataMining = Course().apply {
        name = "Data Mining"
        code = "CS402"
        semester = 4
    }

    private val mobileDevelopment = Course().apply {
        name = "Mobile Development"
        code = "CS403"
        semester = 4
    }

    private val cloudComputing = Course().apply {
        name = "Cloud Computing"
        code = "CS404"
        semester = 5

    }

    private val securityFundamentals = Course().apply {
        name = "Security Fundamentals"
        code = "CS405"
        semester = 5

    }

    private val advancedAlgorithms = Course().apply {
        name = "Advanced Algorithms"
        code = "CS406"
        semester = 5

    }

    private val businessEconomics = Course().apply {
        name = "Business Economics"
        code = "BUS101"
        semester = 1

    }

    private val accounting = Course().apply {
        name = "Accounting Principles"
        code = "BUS102"
        semester = 1

    }

    private val marketing = Course().apply {
        name = "Marketing Fundamentals"
        code = "BUS201"
        semester = 2

    }

    private val managementPrinciples = Course().apply {
        name = "Management Principles"
        code = "BUS202"
        semester = 2

    }

    private val businessLaw = Course().apply {
        name = "Business Law"
        code = "BUS301"
        semester = 3

    }

    private val financialManagement = Course().apply {
        name = "Financial Management"
        code = "BUS302"
        semester = 3

    }

    val allCourses = listOf(
        programmingBasics, dataStructures, algorithms, databaseSystems,
        webDevelopment, artificialIntelligence, machineLearning, dataMining,
        mobileDevelopment, cloudComputing, securityFundamentals, advancedAlgorithms,
        businessEconomics, accounting, marketing, managementPrinciples,
        businessLaw, financialManagement
    )

    private val barcelonaUniversity = University().apply {
        name = "University of Barcelona"
        shortName = "UB"
        country = "Spain"
    }

    private val parisUniversity = University().apply {
        name = "Sorbonne University"
        shortName = "SU"
        country = "France"
    }

    val allUniversities = listOf(barcelonaUniversity, parisUniversity)

    private val csOptionalGroup1 = OptionalCourseGroup().apply {
        name = "Advanced Programming Group"
        semester = 3
        coursesToChoose = mutableListOf(
            Course().apply {
                name = "Functional Programming"
                code = "CS303"
                semester = 3

            },
            Course().apply {
                name = "Object-Oriented Programming"
                code = "CS304"
                semester = 3
            }
        )
    }

    private val csOptionalGroup2 = OptionalCourseGroup().apply {
        name = "Systems Group"
        semester = 4
        coursesToChoose = mutableListOf(
            Course().apply {
                name = "Operating Systems"
                code = "CS407"
                semester = 4

            },
            Course().apply {
                name = "Computer Networks"
                code = "CS408"
                semester = 4

            }
        )
    }

    private val csOptionalGroup3 = OptionalCourseGroup().apply {
        name = "Projects Group"
        semester = 5
        coursesToChoose = mutableListOf(
            Course().apply {
                name = "Team Project"
                code = "CS409"
                semester = 5

            },
            Course().apply {
                name = "Individual Research"
                code = "CS410"
                semester = 5

            }
        )
    }

    private val businessOptionalGroup1 = OptionalCourseGroup().apply {
        name = "Business Communication Group"
        semester = 3
        coursesToChoose = mutableListOf(
            Course().apply {
                name = "Business Communication"
                code = "BUS303"
                semester = 3

            },
            Course().apply {
                name = "Negotiation Skills"
                code = "BUS304"
                semester = 3
            }
        )
    }

    private val businessOptionalGroup2 = OptionalCourseGroup().apply {
        name = "International Business Group"
        semester = 4
        coursesToChoose = mutableListOf(
            Course().apply {
                name = "International Business"
                code = "BUS401"
                semester = 4
            },
            Course().apply {
                name = "Cross-Cultural Management"
                code = "BUS402"
                semester = 4
            }
        )
    }

    private val businessOptionalGroup3 = OptionalCourseGroup().apply {
        name = "Business Analytics Group"
        semester = 5
        coursesToChoose = mutableListOf(
            Course().apply {
                name = "Business Analytics"
                code = "BUS403"
                semester = 5
            },
            Course().apply {
                name = "Market Research"
                code = "BUS404"
                semester = 5
            }
        )
    }

    // Abroad Semesters for Computer Science
    private val csAbroadBarcelona = AbroadSemester().apply {
        semester = 4
        university = barcelonaUniversity
        compulsoryCourses = mutableListOf(
            Course().apply {
                name = "Spanish Computing Terminology"
                code = "CS-BCN-101"
                semester = 4
            },
            Course().apply {
                name = "Mediterranean Tech Innovation"
                code = "CS-BCN-102"
                semester = 4
            }
        )
        optionalCourseGroups = mutableListOf(
            OptionalCourseGroup().apply {
                name = "Barcelona CS Specialization"
                semester = 4
                coursesToChoose = mutableListOf(
                    Course().apply {
                        name = "Smart City Technologies"
                        code = "CS-BCN-201"
                        semester = 4
                    },
                    Course().apply {
                        name = "Mediterranean Tech Startups"
                        code = "CS-BCN-202"
                        semester = 4
                    }
                )
            }
        )
    }

    private val csAbroadParis = AbroadSemester().apply {
        semester = 4
        university = parisUniversity
        compulsoryCourses = mutableListOf(
            Course().apply {
                name = "French Computing Terminology"
                code = "CS-PAR-101"
                semester = 4
            },
            Course().apply {
                name = "European Tech Regulation"
                code = "CS-PAR-102"
                semester = 4
            }
        )
        optionalCourseGroups = mutableListOf(
            OptionalCourseGroup().apply {
                name = "Paris CS Specialization"
                semester = 4
                coursesToChoose = mutableListOf(
                    Course().apply {
                        name = "AI Ethics"
                        code = "CS-PAR-201"
                        semester = 4
                    },
                    Course().apply {
                        name = "French Tech Industry"
                        code = "CS-PAR-202"
                        semester = 4
                    }
                )
            }
        )
    }

    private val businessAbroadBarcelona = AbroadSemester().apply {
        semester = 4
        university = barcelonaUniversity
        compulsoryCourses = mutableListOf(
            Course().apply {
                name = "Spanish Business Terminology"
                code = "BUS-BCN-101"
                semester = 4
            },
            Course().apply {
                name = "Mediterranean Business Culture"
                code = "BUS-BCN-102"
                semester = 4
            }
        )
        optionalCourseGroups = mutableListOf(
            OptionalCourseGroup().apply {
                name = "Barcelona Business Specialization"
                semester = 4
                coursesToChoose = mutableListOf(
                    Course().apply {
                        name = "Tourism Management"
                        code = "BUS-BCN-201"
                        semester = 4
                    },
                    Course().apply {
                        name = "Mediterranean Entrepreneurship"
                        code = "BUS-BCN-202"
                        semester = 4
                    }
                )
            }
        )
    }

    private val businessAbroadParis = AbroadSemester().apply {
        semester = 4
        university = parisUniversity
        compulsoryCourses = mutableListOf(
            Course().apply {
                name = "French Business Terminology"
                code = "BUS-PAR-101"
                semester = 4
            },
            Course().apply {
                name = "European Business Law"
                code = "BUS-PAR-102"
                semester = 4
            }
        )
        optionalCourseGroups = mutableListOf(
            OptionalCourseGroup().apply {
                name = "Paris Business Specialization"
                semester = 4
                coursesToChoose = mutableListOf(
                    Course().apply {
                        name = "Luxury Brand Management"
                        code = "BUS-PAR-201"
                        semester = 4
                    },
                    Course().apply {
                        name = "European Financial Markets"
                        code = "BUS-PAR-202"
                        semester = 4
                    }
                )
            }
        )
    }

    // Subjects
    val computerScience = Subject().apply {
        name = "Computer Science"
        compulsoryCourses = mutableListOf(
            programmingBasics,    // Semester 1
            dataStructures,       // Semester 2
            databaseSystems,      // Semester 2
            algorithms,           // Semester 3
            webDevelopment,       // Semester 3
            cloudComputing,       // Semester 5
            securityFundamentals  // Semester 5
        )
        optionalCourseGroups = mutableListOf(
            csOptionalGroup1,     // Semester 3
            csOptionalGroup2,     // Semester 4
            csOptionalGroup3      // Semester 5
        )
        abroadSemestersToChoose = mutableListOf(
            csAbroadBarcelona,    // Semester 4
            csAbroadParis         // Semester 4
        )
    }

    val businessAdministration = Subject().apply {
        name = "Business Administration"
        compulsoryCourses = mutableListOf(
            businessEconomics,    // Semester 1
            accounting,           // Semester 1
            marketing,            // Semester 2
            managementPrinciples, // Semester 2
            businessLaw,          // Semester 3
            financialManagement   // Semester 3
        )
        optionalCourseGroups = mutableListOf(
            businessOptionalGroup1, // Semester 3
            businessOptionalGroup2, // Semester 4
            businessOptionalGroup3  // Semester 5
        )
        abroadSemestersToChoose = mutableListOf(
            businessAbroadBarcelona, // Semester 4
            businessAbroadParis      // Semester 4
        )
    }

    val allSubjects = listOf(computerScience, businessAdministration)
}