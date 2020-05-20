package org.zoop.personhandler.restdto

class HobbiesDTO (val _hobby_list : MutableList<HobbyDTO>) {
    private val hobbyList = _hobby_list

    override fun toString(): String {
        val result = StringBuilder("")
        if (hobbyList.isNotEmpty()) {
            hobbyList.forEach { h ->
                result.append("${h.hobby_name} (${h.complexity})\n")
            }
        } else result.append("(Has no hobbies)\n")

        return result.toString()
    }
}
