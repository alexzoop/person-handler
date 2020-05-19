package org.zoop.personhandler.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

object DateFormatter {
    val dateFormat : DateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun isValid(date : String) : Boolean {
        try {
            dateFormat.parse(date)
        } catch (e : ParseException) {
            return false
        }
        return true
    }
}