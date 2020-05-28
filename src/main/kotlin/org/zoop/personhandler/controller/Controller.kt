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
    //@GetMapping(value = ["/", "/index"])
    @RequestMapping(value = ["/", "/index"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("message", welcomeMessage)
        return "index"
    }

    @RequestMapping(value = ["/personList"], method = [RequestMethod.GET])
    fun personList(model: Model,
                   @RequestParam(value = "upload", defaultValue = "none") message : String
    ): String {
        when (message) {
            "s" -> {model.addAttribute("message", "File has successfully uploaded")
            }
            "sw" -> {model.addAttribute("message", "Something wrong with your file")
            }
            "ef" -> {model.addAttribute("message", "File is empty")}
        }
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
            model: Model
    ): String {
        val hobbyAddForm = HobbyAddForm()
        hobbyAddForm.personId = id
        model.addAttribute("hobbyAddForm", hobbyAddForm)
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
                !hobby_name.isNullOrEmpty()
                && complexity.isNullOrEmpty()
                && complexity?.toIntOrNull() != null
                && complexity.toInt() > 0
        ) {
            dbServices.addHobbyForm(hobbyAddForm)
            return "redirect:/hobbyList?personid=${personId}"
        }
        model.addAttribute("errorMessage", errorMessage)
        return "addHobby"
    }

    @RequestMapping(value = ["/upload"], method = [RequestMethod.POST])
    fun postFileUpload(@RequestParam("file") file: MultipartFile,
                         model: Model
    ): String? {
        val message : String
        message = if (!file.isEmpty) {
            try {
                val bytes = file.bytes
                val stream = BufferedOutputStream(FileOutputStream(File("xmlData/input/${file.originalFilename}")))
                stream.write(bytes)
                stream.close()
                "s"

            } catch (e: Exception) {
                "sw"
            }
        } else {
            "ef"
        }
        return "redirect:/personList?upload=$message"
    }
}
