package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.zoop.personhandler.dbentities.DbServices

@EnableScheduling
@Component
class RabbitClient (val template : RabbitTemplate, val dbServices: DbServices){

    @Scheduled(fixedDelay = 1000)
    fun requestAccounts() {
        val count = dbServices.getQuantity()
        for (id in 1..count) {
            if (dbServices.isEmptyAccount(id)) {
                template.convertAndSend("request-queue", dbServices.getNameById(id)!!)
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    fun assignAccounts() {
        val count = dbServices.getQuantity()
        for (id in 1..count) {
            if (dbServices.isEmptyAccount(id)) {
                val account = template.receiveAndConvert("answer-queue") as Int

                dbServices.updateAccount(id, account)
            }
        }
    }
}