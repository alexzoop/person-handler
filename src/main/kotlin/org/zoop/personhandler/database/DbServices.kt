package org.zoop.personhandler.database

import org.springframework.stereotype.Component
import org.zoop.personhandler.controller.forms.HobbyAddForm
import org.zoop.personhandler.controller.forms.PersonAddForm
import org.zoop.personhandler.database.entities.HobbyEntity
import org.zoop.personhandler.database.entities.PersonEntity
import org.zoop.personhandler.database.entities.PersonsDb
import org.zoop.personhandler.database.repo.HobbyRepository
import org.zoop.personhandler.database.repo.PersonRepository
import org.zoop.personhandler.controller.restdto.*
import org.zoop.personhandler.utils.DateFormatter
import org.zoop.personhandler.utils.copyEntities
import org.zoop.personhandler.xmlentities.unmarshalData

@Component
class DbServices(
        val personRepository: PersonRepository,
        val hobbyRepository: HobbyRepository) {

    fun unmarshallAndInsertData(filepath: String): Boolean {
        try {
            val insertedData: PersonsDb? = copyEntities(unmarshalData(filepath))
            personRepository.saveAll(insertedData!!.listOfPersons)
            println("Data from $filepath have been successfully written into database")
            return true
        } catch (e: Exception) {
            println("Data from $filepath doesn't been written into database because of ${e.message}")
        }
        return false
    }

    fun isEmptyAccount(id: Long) = personRepository.findById(id).get().personal_account == null

    fun getListOfEmptyAccounts(): List<Long> {
        val list = mutableListOf<Long>()
        personRepository.getAllId().forEach {
            if (isEmptyAccount(it)) list.add(it)
        }
        return list
    }

    fun updateAccount(id: Long, personalAccount: Int) {
        if (personRepository.findById(id).isPresent) {
            val person = personRepository.findById(id).get()
            if (person.personal_account == null) {
                person.personal_account = personalAccount
                personRepository.save(person)
            }
            println("Account of ${person.name} has successfully updated")
        } else println("ERROR! Cannot find person with id = $id in persons_db")
    }

    fun getAllPersons(): PersonsDb {
        val personsFromRepo: MutableIterable<PersonEntity> = personRepository.findAll()
        var personsDb = PersonsDb()
        personsFromRepo.forEach {
            personsDb.listOfPersons.add(it)
        }
        return personsDb
    }

    fun toHobbyDTO(hobbyEntity: HobbyEntity) = HobbyDTO(
            hobbyEntity.id,
            hobbyEntity.hobby_name,
            hobbyEntity.complexity,
            hobbyEntity.person_entity?.id
    )

    fun getHobbiesDTO(personEntity: PersonEntity): HobbiesDTO {
        val hobbyList: MutableList<HobbyDTO> = arrayListOf()
        personEntity.hobbies.forEach { hobbyList.add(toHobbyDTO(it)) }
        return HobbiesDTO(hobbyList)
    }

    fun toPersonDTO(personEntity: PersonEntity) = PersonDTO(
            personEntity.id,
            personEntity.name,
            DateFormatter.dateFormat.format(personEntity.birthday),
            personEntity.personal_account,
            getHobbiesDTO(personEntity)
    )

    fun getPersonDTO(id: Long): PersonDTO? {
        if (personRepository.findById(id).isPresent) {
            val person = personRepository.findById(id).get()
            return toPersonDTO(person)
        } else println("ERROR! Cannot find person with id = $id in persons_db")
        return null
    }

    fun getNameMap(id: Long): Map<Long?, String?>? {
        if (personRepository.findById(id).isPresent) {
            val person = personRepository.findById(id).get()
            val personDTO = mapOf(person.id to person.name)
            println("Account of ${person.name} has successfully updated")
            return personDTO
        } else println("ERROR! Cannot find person with id = $id in persons_db")
        return null

    }

    fun getAllPersonsDTO(): PersonsDTO {
        val persons = getAllPersons()
        var personsList: MutableList<PersonDTO> = arrayListOf()
        persons.listOfPersons.forEach {
            personsList.add(toPersonDTO(it))
        }
        return PersonsDTO(personsList)
    }

    fun addPersonForm(personAddForm: PersonAddForm) {
        val personEntity = PersonEntity()
        personEntity.name = personAddForm.name
        personEntity.birthday = DateFormatter.dateFormat.parse(personAddForm.birthday)
        personRepository.save(personEntity)
    }

    fun isValidId(id: Long) = personRepository.existsById(id)

    fun deleteById(id: Long) {
        if (isValidId(id)) personRepository.deleteById(id)
        else println("ERROR! Cannot find person with id = $id in persons_db")
    }

    fun deleteHobby(id: Long?) : Long? {
        val personId = hobbyRepository.findById(id!!).get().person_entity?.id
        hobbyRepository.deleteById(id)
        return personId
    }

    fun addHobbyForm(hobbyAddForm: HobbyAddForm) {
        val hobbyEntity = HobbyEntity()
        hobbyEntity.hobby_name = hobbyAddForm.hobby_name
        hobbyEntity.complexity = hobbyAddForm.complexity!!.toInt()
        hobbyEntity.person_entity = personRepository.findById(hobbyAddForm.personId!!).get()
        hobbyRepository.save(hobbyEntity)
    }
}