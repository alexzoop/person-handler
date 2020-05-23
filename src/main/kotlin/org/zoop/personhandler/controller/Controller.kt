package org.zoop.personhandler.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.zoop.personhandler.controller.forms.HobbyAddForm
import org.zoop.personhandler.controller.forms.PersonAddForm
import org.zoop.personhandler.database.DbServices
import org.zoop.personhandler.utils.DateFormatter
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


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
            @RequestParam(value = "personid") id: Long,
            model: Model): String {
        model.addAttribute("PersonDTO", dbServices.getPersonDTO(id))
        return "hobbyList"
    }

    @RequestMapping("/deleteHobby")
    fun deleteHobby(@RequestParam(value = "id") id: Long): String {
        val personId = dbServices.deleteHobby(id)
        return "redirect:/hobbyList?personid=$personId"
    }

    @RequestMapping(value = ["/addHobby"], method = [RequestMethod.GET])
    fun showAddHobbyPage(
            @RequestParam(value = "personid") id: Long,
            @ModelAttribute("errorMessage") error : String?,
            model: Model
    ): String {
        val hobbyAddForm = HobbyAddForm()
        hobbyAddForm.personId = id
        model.addAttribute("hobbyAddForm", hobbyAddForm)
        model.addAttribute("errorMessage", error)
        return "addHobby"
    }

    @RequestMapping(value = ["/addHobby"], method = [RequestMethod.POST])
    fun saveHobby(
            model: Model,
            @ModelAttribute("hobbyAddForm") hobbyAddForm: HobbyAddForm
    ): String {
        val hobby_name = hobbyAddForm.hobby_name
        val complexity = hobbyAddForm.complexity
        val personId = hobbyAddForm.personId
        if (
                hobby_name!!.isNotEmpty()
               && complexity?.toInt()!! >= 0
        ) {
            dbServices.addHobbyForm(hobbyAddForm)
            return "redirect:/hobbyList?personid=${hobbyAddForm.personId}"
        }
        model.addAttribute("errorMessage", errorMessage)

        return "redirect:/addHobby?personid=$personId"
    }

    @RequestMapping(value = ["/upload"], method = [RequestMethod.POST])
    fun postFileUpload(@RequestParam("file") file: MultipartFile,
                         model: Model
    ): String? {
        if (!file.isEmpty) {
            try {
                val bytes = file.bytes
                val stream = BufferedOutputStream(FileOutputStream(File("xmlData/input/${file.originalFilename}")))
                stream.write(bytes)
                stream.close()
                model.addAttribute("uploadMessage", "${file.originalFilename} has successfully uploaded")

            } catch (e: Exception) {
                model.addAttribute("uploadMessage", "Wrong file ${file.originalFilename}!")
            }
        } else {
            model.addAttribute("uploadMessage", "${file.name} is empty")
        }
        return "redirect:/personList"
    }
}
