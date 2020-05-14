package org.zoop.personhandler.dbentities

import org.springframework.stereotype.Component
import org.zoop.personhandler.utils.copyEntities
import org.zoop.personhandler.xmlentities.unmarshalData

@Component
class DbServices (val personRepository: PersonRepository) {
    fun unmarshallAndInsertData (filepath : String) : Boolean {
        try {
            val insertedData: PersonsDb? = copyEntities(unmarshalData(filepath))
            personRepository.saveAll(insertedData!!.listOfPersons)
            println("Data from $filepath have been successfully written into database")
            return true
        } catch (e : Exception) {
            println("Data from $filepath doesn't been written into database because of ${e.message}")
        }
        return false
    }

    fun getQuantity() : Long {
        return personRepository.count()
    }

    fun getNameById(id : Long) : String? {
        return if (personRepository.findById(id).isPresent)
            personRepository.findById(id).get().name!!
        else {
            println("ERROR! Cannot find person with id = $id in persons_db")
            null
        }
    }

    fun isEmptyAccount(id : Long) : Boolean {
        return personRepository.findById(id).get().personal_account == null
    }

    fun getListOfEmptyAccounts() : List<Long> {
        val count = getQuantity()
        val list = mutableListOf<Long>()
        for (id in 1..count) if (isEmptyAccount(id)) list.add(id)
        return list
    }


    fun updateAccount (id: Long, personalAccount : Int) {
        if (personRepository.findById(id).isPresent) {
            val person = personRepository.findById(id).get()
            if (person.personal_account == null) {
                person.personal_account = personalAccount
                personRepository.save(person)
            }
            println("Account of ${person.name} has successfully updated")
        } else println("ERROR! Cannot find person with id = $id in persons_db")
    }
}