package org.zoop.personhandler.dbentities

import org.zoop.personhandler.utils.DateFormatter.dateFormat

class PersonsDb {
    var listOfPersons : MutableList<PersonEntity> = ArrayList()

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