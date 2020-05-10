package org.zoop.personhandler.utils

import org.zoop.personhandler.utils.DateFormatter.dateFormat
import java.util.*
import javax.xml.bind.annotation.adapters.XmlAdapter

class DateAdapter : XmlAdapter<String, Date>() {

    override fun unmarshal(v: String?): Date {
        return dateFormat.parse(v)
    }

    override fun marshal(v: Date?): String {
        return dateFormat.format(v)
    }
}