package org.zoop.personhandler.database.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.zoop.personhandler.database.entities.HobbyEntity

@Repository
interface HobbyRepository : CrudRepository<HobbyEntity, Long> {

}