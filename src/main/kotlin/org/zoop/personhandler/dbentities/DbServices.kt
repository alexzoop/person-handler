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
}