package dev.rr.moduleselectorapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ModuleSelectorAppApplication

fun main(args: Array<String>) {
    runApplication<ModuleSelectorAppApplication>(*args)
}
