package org.zoop.personhandler.database.entities

import javax.persistence.*

@Entity
@Table(name = "hobbies")
class HobbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @Column(name = "hobby_name")
    var hobby_name : String? = null

    @Column(name = "complexity")
    var complexity : Int? = null

    @ManyToOne
    @JoinColumn(name = "person_id")
    var person_entity : PersonEntity? = null
}