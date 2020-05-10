package org.zoop.personhandler.xmlentities

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
class Hobby {
    var hobby_name : String? = null

    var complexity : Int? = null
}