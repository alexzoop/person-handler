package org.zoop.personhandler.database.repo

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.zoop.personhandler.database.entities.PersonEntity

@Repository
interface PersonRepository : CrudRepository<PersonEntity, Long> {
    @Query(value = "select id from persons", nativeQuery = true)
    fun getAllId() : List<Long>
}