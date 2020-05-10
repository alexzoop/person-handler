package org.zoop.personhandler.xmlentities

import java.io.BufferedReader
import java.io.File
import javax.xml.bind.JAXBContext

val jaxbContext: JAXBContext = JAXBContext.newInstance(Persons::class.java)

fun unmarshalData (filename: String) : Persons? {
    val unmarshaller = jaxbContext.createUnmarshaller()
    var fileReader : BufferedReader? = null
    var personsFromXml : Persons? = null

    try {
        fileReader = File(filename).bufferedReader()
        personsFromXml = unmarshaller.unmarshal(fileReader) as Persons
        println("Data was successfully read from $filename")
        println(personsFromXml.toString())
    } catch (e : Exception) {
        println("Failed to read data from $filename (${e.message})")
    } finally {
        fileReader?.close()
        return personsFromXml
    }

}