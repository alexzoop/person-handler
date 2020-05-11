package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.zoop.personhandler.dbentities.DbServices

@Component
class RabbitClient (@Autowired val template : RabbitTemplate, val dbServices: DbServices){
    fun assignAccounts() {
        val count = dbServices.getQuantity()
        for (id in 1..count) {
            val account = template.convertSendAndReceive("account-queue", dbServices.getNameById(id)) as Int
            dbServices.updateAccount(id, account)
        }
    }
}