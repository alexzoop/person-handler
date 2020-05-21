package org.zoop.personhandler.utils

import org.zoop.personhandler.database.entities.HobbyEntity
import org.zoop.personhandler.database.entities.PersonEntity
import org.zoop.personhandler.database.entities.PersonsDb
import org.zoop.personhandler.xmlentities.Persons

fun copyEntities (deserializedPersons: Persons?) : PersonsDb {
    val entities = PersonsDb()
    deserializedPersons!!.listOfPersons.forEach {xmlPerson ->
        val personEntity = PersonEntity()
        personEntity.name = xmlPerson.name
        personEntity.birthday = xmlPerson.birthday

        xmlPerson.hobbies.forEach { xmlHobby ->
            val hobbyEntity = HobbyEntity()
            hobbyEntity.hobby_name = xmlHobby.hobby_name
            hobbyEntity.complexity = xmlHobby.complexity
            hobbyEntity.person_entity = personEntity
            personEntity.hobbies.add(hobbyEntity)
        }
        entities.listOfPersons.add(personEntity)
    }

    return entities
}