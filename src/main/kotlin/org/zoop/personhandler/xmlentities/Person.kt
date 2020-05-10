package org.zoop.personhandler.xmlentities

import org.springframework.format.annotation.DateTimeFormat
import org.zoop.personhandler.utils.DateAdapter
import java.util.*
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlAccessorType(XmlAccessType.FIELD)
class Person {
    var name : String? = null

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @XmlJavaTypeAdapter(DateAdapter::class)
    var birthday : Date? = null

    @XmlElementWrapper
    @XmlElement(name = "hobby")
    var hobbies: List<Hobby> = ArrayList()
}