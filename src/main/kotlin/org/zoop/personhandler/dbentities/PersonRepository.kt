package org.zoop.personhandler.dbentities

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : CrudRepository<PersonEntity, Long> {
    @Query(value = "select id from persons", nativeQuery = true)
    fun getAllId() : List<Long>
}