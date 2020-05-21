package org.zoop.personhandler.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.zoop.personhandler.dbentities.DbServices
import org.zoop.personhandler.controller.forms.PersonAddForm
import org.zoop.personhandler.utils.DateFormatter


@Controller
class Controller(val dbServices: DbServices) {

    @Value("\${welcome.message}")
    lateinit var welcomeMessage: String

    @Value("\${error.message}")
    lateinit var errorMessage: String

    @RequestMapping(value = ["/", "/index"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("message", welcomeMessage)
        return "index"
    }

    @RequestMapping(value = ["/personList"], method = [RequestMethod.GET])
    fun personList(model: Model): String {
        model.addAttribute("persons", dbServices.getAllPersonsDTO().listOfPersons)
        return "personList"
    }

    @RequestMapping(value = ["/addPerson"], method = [RequestMethod.GET])
    fun showAddPersonPage(model: Model): String {
        val personAddForm = PersonAddForm()
        model.addAttribute("personAddForm", personAddForm)
        return "addPerson"
    }

    @RequestMapping(value = ["/addPerson"], method = [RequestMethod.POST])
    fun savePerson(
            model: Model,
            @ModelAttribute("personAddForm") personAddForm: PersonAddForm
    ): String {
        val name: String? = personAddForm.name
        val birthday: String? = personAddForm.birthday
        if (
                name != null
                && name.isNotEmpty()
                && birthday != null
                && birthday.isNotEmpty()
                && DateFormatter.isValid(birthday)
        ) {
            dbServices.addPersonForm(personAddForm)
            return "redirect:/personList"
        }
        model.addAttribute("errorMessage", errorMessage)
        return "addPerson"
    }

    @RequestMapping("/delete")
    fun deletePerson(@RequestParam(value = "id") id: Long): String {
        if (dbServices.isValidId(id)) dbServices.deleteById(id)
        return "redirect:/personList"
    }

    @RequestMapping(value = ["/hobbyList"], method = [RequestMethod.GET])
    fun hobbyList(
            @RequestParam(value = "id") id: Long,
            model: Model): String {
        model.addAttribute("PersonDTO", dbServices.getPersonDTO(id))
        return "hobbyList"
    }

    @RequestMapping("/deleteHobby")
    fun deleteHobby(@RequestParam(value = "id") id: Long): String {
        if (dbServices.isValidId(id)) dbServices.deleteById(id)
        return "redirect:/hobbyList?id=$id"
    }
}
