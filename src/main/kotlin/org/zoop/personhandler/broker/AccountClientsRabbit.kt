package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.zoop.personhandler.dbentities.DbServices

class AccountClientsRabbit(
        val template: RabbitTemplate,
        val dbServices: DbServices) : AccountClients {

    override fun requestAccounts() {
        dbServices.getListOfEmptyAccounts().forEach { id ->
            template.convertAndSend("request-queue", dbServices.getNameMap(id)!!)
        }
    }

    override fun assignAccounts(message : Map<Long, Int>) {
        dbServices.updateAccount(message.keys.elementAt(0), message.values.elementAt(0))
    }
}