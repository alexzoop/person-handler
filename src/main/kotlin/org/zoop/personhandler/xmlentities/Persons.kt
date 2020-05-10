package org.zoop.personhandler.xmlentities

import org.zoop.personhandler.utils.DateFormatter.dateFormat
import javax.xml.bind.annotation.*

@XmlRootElement(name="persons")
@XmlAccessorType(XmlAccessType.FIELD)
class Persons {
    @XmlElement(name = "person")
    var listOfPersons : List<Person> =  ArrayList()

    override fun toString(): String {
        val result = StringBuilder("Persons data:\n")
        listOfPersons.forEach {p ->
            result.append("${p.name} was born ${dateFormat.format(p.birthday)}. ")
            if (p.hobbies.isNotEmpty()) {
                result.append("Hobbies:\n")
                p.hobbies.forEach {
                    result.append("\t${it.hobby_name}\t${it.complexity}\n")
                }
            } else result.append("Has no hobbies.\n") }
        return result.toString()
    }
}