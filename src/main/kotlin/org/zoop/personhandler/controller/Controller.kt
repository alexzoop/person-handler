package org.zoop.personhandler.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.zoop.personhandler.dbentities.DbServices

@RestController
@RequestMapping("persons")
class Controller (val dbServices: DbServices) {

    @RequestMapping("/")
    fun allPersons() = dbServices.getAllPersonsDTO()
}