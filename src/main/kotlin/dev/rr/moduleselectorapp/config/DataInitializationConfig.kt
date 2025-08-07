package dev.rr.moduleselectorapp.config

import dev.rr.moduleselectorapp.common.TempData
import dev.rr.moduleselectorapp.subject.repository.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional

@Configuration
class DataInitializationConfig(
    private val universityRepository: UniversityRepository,
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
        if (subjectRepository.count() > 0) {
            logger.info("Database already initialized, skipping data initialization")
            return
        }

        logger.info("Starting database initialization...")

        TempData.allUniversities.forEach { university ->
            universityRepository.save(university)
        }
        logger.info("Saved ${TempData.allUniversities.size} universities")

        TempData.allSubjects.forEach { subject ->
            subjectRepository.save(subject)
        }
        logger.info("Saved ${TempData.allSubjects.size} subjects with all courses and relationships")

        logger.info("Database initialization completed successfully")
    }
}