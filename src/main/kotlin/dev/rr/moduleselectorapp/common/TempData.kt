package dev.rr.moduleselectorapp.common

import dev.rr.moduleselectorapp.subject.model.AbroadSemester
import dev.rr.moduleselectorapp.subject.model.Course
import dev.rr.moduleselectorapp.subject.model.OptionalCourseGroup
import dev.rr.moduleselectorapp.subject.model.Subject
import dev.rr.moduleselectorapp.subject.model.University

object TempData {

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

    val computerScience = Subject().apply {
        name = "Computer Science"

        val subject = this

        val compulsoryCourses = mutableListOf(
            Course().apply {
                name = "Programming Basics"
                code = "CS101"
                semester = 1
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Data Structures"
                code = "CS102"
                semester = 2
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Algorithms"
                code = "CS201"
                semester = 3
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Database Systems"
                code = "CS202"
                semester = 2
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Web Development"
                code = "CS301"
                semester = 3
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Cloud Computing"
                code = "CS404"
                semester = 5
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Security Fundamentals"
                code = "CS405"
                semester = 5
                isCompulsory = true
                this.subject = subject
            }
        )

        val optionalGroup1 = OptionalCourseGroup().apply {
            semester = 3
        }
        val optionalCourses1 = mutableListOf(
            Course().apply {
                name = "Functional Programming"
                code = "CS303"
                semester = 3
                isCompulsory = false
                optionalGroup = optionalGroup1
            },
            Course().apply {
                name = "Object-Oriented Programming"
                code = "CS304"
                semester = 3
                isCompulsory = false
                optionalGroup = optionalGroup1
            }
        )
        optionalGroup1.coursesToChoose = optionalCourses1

        val optionalGroup2 = OptionalCourseGroup().apply {
            semester = 4
        }
        val optionalCourses2 = mutableListOf(
            Course().apply {
                name = "Operating Systems"
                code = "CS407"
                semester = 4
                isCompulsory = false
                optionalGroup = optionalGroup2
            },
            Course().apply {
                name = "Computer Networks"
                code = "CS408"
                semester = 4
                isCompulsory = false
                optionalGroup = optionalGroup2
            }
        )
        optionalGroup2.coursesToChoose = optionalCourses2

        val optionalGroup3 = OptionalCourseGroup().apply {
            semester = 5
        }
        val optionalCourses3 = mutableListOf(
            Course().apply {
                name = "Team Project"
                code = "CS409"
                semester = 5
                isCompulsory = false
                optionalGroup = optionalGroup3
            },
            Course().apply {
                name = "Individual Research"
                code = "CS410"
                semester = 5
                isCompulsory = false
                optionalGroup = optionalGroup3
            }
        )
        optionalGroup3.coursesToChoose = optionalCourses3

        val abroadBarcelona = AbroadSemester().apply {
            semester = 4
            university = barcelonaUniversity
        }
        val abroadBarcelonaCourses = mutableListOf(
            Course().apply {
                name = "Spanish Computing Terminology"
                code = "CS-BCN-101"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadBarcelona
            },
            Course().apply {
                name = "Mediterranean Tech Innovation"
                code = "CS-BCN-102"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadBarcelona
            }
        )
        abroadBarcelona.courses = abroadBarcelonaCourses

        val abroadBarcelonaOptionalGroup = OptionalCourseGroup().apply {
            semester = 4
        }
        val abroadBarcelonaOptionalCourses = mutableListOf(
            Course().apply {
                name = "Smart City Technologies"
                code = "CS-BCN-201"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadBarcelonaOptionalGroup
            },
            Course().apply {
                name = "Mediterranean Tech Startups"
                code = "CS-BCN-202"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadBarcelonaOptionalGroup
            }
        )
        abroadBarcelonaOptionalGroup.coursesToChoose = abroadBarcelonaOptionalCourses
        abroadBarcelona.optionalCourseGroups = mutableListOf(abroadBarcelonaOptionalGroup)

        val abroadParis = AbroadSemester().apply {
            semester = 4
            university = parisUniversity
        }
        val abroadParisCourses = mutableListOf(
            Course().apply {
                name = "French Computing Terminology"
                code = "CS-PAR-101"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadParis
            },
            Course().apply {
                name = "European Tech Regulation"
                code = "CS-PAR-102"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadParis
            }
        )
        abroadParis.courses = abroadParisCourses

        val abroadParisOptionalGroup = OptionalCourseGroup().apply {
            semester = 4
        }
        val abroadParisOptionalCourses = mutableListOf(
            Course().apply {
                name = "AI Ethics"
                code = "CS-PAR-201"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadParisOptionalGroup
            },
            Course().apply {
                name = "French Tech Industry"
                code = "CS-PAR-202"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadParisOptionalGroup
            }
        )
        abroadParisOptionalGroup.coursesToChoose = abroadParisOptionalCourses
        abroadParis.optionalCourseGroups = mutableListOf(abroadParisOptionalGroup)

        courses = (compulsoryCourses + optionalCourses1 + optionalCourses2 + optionalCourses3 + abroadBarcelonaCourses + abroadBarcelonaOptionalCourses + abroadParisCourses + abroadParisOptionalCourses).toMutableList()
        optionalCourseGroups = mutableListOf(optionalGroup1, optionalGroup2, optionalGroup3)
        abroadSemestersToChoose = mutableListOf(abroadBarcelona, abroadParis)
    }

    val businessAdministration = Subject().apply {
        name = "Business Administration"

        val subject = this

        val compulsoryCourses = mutableListOf(
            Course().apply {
                name = "Business Economics"
                code = "BUS101"
                semester = 1
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Accounting Principles"
                code = "BUS102"
                semester = 1
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Marketing Fundamentals"
                code = "BUS201"
                semester = 2
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Management Principles"
                code = "BUS202"
                semester = 2
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Business Law"
                code = "BUS301"
                semester = 3
                isCompulsory = true
                this.subject = subject
            },
            Course().apply {
                name = "Financial Management"
                code = "BUS302"
                semester = 3
                isCompulsory = true
                this.subject = subject
            }
        )

        val optionalGroup1 = OptionalCourseGroup().apply {
            semester = 3
        }
        val optionalCourses1 = mutableListOf(
            Course().apply {
                name = "Business Communication"
                code = "BUS303"
                semester = 3
                isCompulsory = false
                optionalGroup = optionalGroup1
            },
            Course().apply {
                name = "Negotiation Skills"
                code = "BUS304"
                semester = 3
                isCompulsory = false
                optionalGroup = optionalGroup1
            }
        )
        optionalGroup1.coursesToChoose = optionalCourses1

        val optionalGroup2 = OptionalCourseGroup().apply {
            semester = 4
        }
        val optionalCourses2 = mutableListOf(
            Course().apply {
                name = "International Business"
                code = "BUS401"
                semester = 4
                isCompulsory = false
                optionalGroup = optionalGroup2
            },
            Course().apply {
                name = "Cross-Cultural Management"
                code = "BUS402"
                semester = 4
                isCompulsory = false
                optionalGroup = optionalGroup2
            }
        )
        optionalGroup2.coursesToChoose = optionalCourses2

        val optionalGroup3 = OptionalCourseGroup().apply {
            semester = 5
        }
        val optionalCourses3 = mutableListOf(
            Course().apply {
                name = "Business Analytics"
                code = "BUS403"
                semester = 5
                isCompulsory = false
                optionalGroup = optionalGroup3
            },
            Course().apply {
                name = "Market Research"
                code = "BUS404"
                semester = 5
                isCompulsory = false
                optionalGroup = optionalGroup3
            }
        )
        optionalGroup3.coursesToChoose = optionalCourses3

        val abroadBarcelona = AbroadSemester().apply {
            semester = 4
            university = barcelonaUniversity
        }
        val abroadBarcelonaCourses = mutableListOf(
            Course().apply {
                name = "Spanish Business Terminology"
                code = "BUS-BCN-101"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadBarcelona
            },
            Course().apply {
                name = "Mediterranean Business Culture"
                code = "BUS-BCN-102"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadBarcelona
            }
        )
        abroadBarcelona.courses = abroadBarcelonaCourses

        val abroadBarcelonaOptionalGroup = OptionalCourseGroup().apply {
            semester = 4
        }
        val abroadBarcelonaOptionalCourses = mutableListOf(
            Course().apply {
                name = "Tourism Management"
                code = "BUS-BCN-201"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadBarcelonaOptionalGroup
            },
            Course().apply {
                name = "Mediterranean Entrepreneurship"
                code = "BUS-BCN-202"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadBarcelonaOptionalGroup
            }
        )
        abroadBarcelonaOptionalGroup.coursesToChoose = abroadBarcelonaOptionalCourses
        abroadBarcelona.optionalCourseGroups = mutableListOf(abroadBarcelonaOptionalGroup)

        val abroadParis = AbroadSemester().apply {
            semester = 4
            university = parisUniversity
        }
        val abroadParisCourses = mutableListOf(
            Course().apply {
                name = "French Business Terminology"
                code = "BUS-PAR-101"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadParis
            },
            Course().apply {
                name = "European Business Law"
                code = "BUS-PAR-102"
                semester = 4
                isCompulsory = true
                abroadSemester = abroadParis
            }
        )
        abroadParis.courses = abroadParisCourses

        val abroadParisOptionalGroup = OptionalCourseGroup().apply {
            semester = 4
        }
        val abroadParisOptionalCourses = mutableListOf(
            Course().apply {
                name = "Luxury Brand Management"
                code = "BUS-PAR-201"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadParisOptionalGroup
            },
            Course().apply {
                name = "European Financial Markets"
                code = "BUS-PAR-202"
                semester = 4
                isCompulsory = false
                optionalGroup = abroadParisOptionalGroup
            }
        )
        abroadParisOptionalGroup.coursesToChoose = abroadParisOptionalCourses
        abroadParis.optionalCourseGroups = mutableListOf(abroadParisOptionalGroup)

        courses = (compulsoryCourses + optionalCourses1 + optionalCourses2 + optionalCourses3 + abroadBarcelonaCourses + abroadBarcelonaOptionalCourses + abroadParisCourses + abroadParisOptionalCourses).toMutableList()
        optionalCourseGroups = mutableListOf(optionalGroup1, optionalGroup2, optionalGroup3)
        abroadSemestersToChoose = mutableListOf(abroadBarcelona, abroadParis)
    }

    val allSubjects = listOf(computerScience, businessAdministration)
}