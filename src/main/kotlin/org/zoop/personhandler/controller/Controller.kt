package org.zoop.personhandler.controller

import org.springframework.integration.support.json.JsonObjectMapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.zoop.personhandler.dbentities.DbServices

@RestController
@RequestMapping("persons")
class Controller (val dbServices: DbServices) {

    @RequestMapping("/")
    fun allPersons() = dbServices.getAllPersons()
}