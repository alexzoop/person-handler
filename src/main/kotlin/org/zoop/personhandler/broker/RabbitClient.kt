package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.zoop.personhandler.dbentities.DbServices

@EnableScheduling
@Component
class RabbitClient (val template : RabbitTemplate, val dbServices: DbServices){

    @Scheduled(fixedDelay = 10000)
    fun assignAccounts() {
        val count = dbServices.getQuantity()
        for (id in 1..count) {
            if (dbServices.isEmptyAccount(id)) {
                val account = template.convertSendAndReceive("account-queue", dbServices.getNameById(id)!!) as Int
                dbServices.updateAccount(id, account)
            }
        }
    }
}