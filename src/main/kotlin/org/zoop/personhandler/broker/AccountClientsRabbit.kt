package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.zoop.personhandler.dbentities.DbServices

class AccountClientsRabbit(
        val template: RabbitTemplate,
        val dbServices: DbServices,
        val rabbitAdmin: RabbitAdmin) : AccountClients {

    override fun requestAccounts() {
        val emptyAccountList = dbServices.getListOfEmptyAccounts()

        if (rabbitAdmin.getQueueInfo("request-queue").messageCount < emptyAccountList.size)
            emptyAccountList.forEach { id ->
                template.convertAndSend("request-queue", dbServices.getNameMap(id)!!)
            }
    }

    override fun assignAccounts(message: Map<Long, Int>) {
        dbServices.updateAccount(message.keys.elementAt(0), message.values.elementAt(0))
    }
}