package org.zoop.personhandler.controller.restdto

data class PersonDTO(
        val id : Long?,
        val name : String?,
        val birthday : String?,
        val account : Int?,
        val hobbies : HobbiesDTO?
)