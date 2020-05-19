package org.zoop.personhandler.restdto

data class PersonDTO(
        val id : Long?,
        val name : String?,
        val birthday : String?,
        val account : Int?
        //val hobbies : List<HobbyDTO>?
)