package org.zoop.personhandler.dbentities

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : CrudRepository<PersonEntity, Long> {

}