package org.zoop.personhandler.database.entities

import org.springframework.format.annotation.DateTimeFormat
import org.zoop.personhandler.database.entities.HobbyEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "persons")
class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id :Long? = null

    @Column(name = "name")
    var name : String? = null

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    var birthday : Date? = null

    @OneToMany(mappedBy = "person_entity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var hobbies: MutableList<HobbyEntity> = ArrayList()

    @Column(name = "personal_account")
    var personal_account : Int? = null
}