package org.zoop.personhandler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonHandlerApplication

fun main(args: Array<String>) {
    runApplication<PersonHandlerApplication>(*args)
}
