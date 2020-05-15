package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.zoop.personhandler.dbentities.DbServices

@EnableScheduling
@Component
class RabbitClients (val template : RabbitTemplate, val dbServices: DbServices, val rabbitAdmin: RabbitAdmin){

    @Scheduled(fixedDelay = 5000)
    fun requestAccounts() {
        if (rabbitAdmin.getQueueInfo("request-queue").messageCount < dbServices.getListOfEmptyAccounts().size) {
            rabbitAdmin.purgeQueue("request-queue")
            dbServices.getListOfEmptyAccounts().forEach { id ->
                val requestMap = mapOf(id to dbServices.getNameById(id))
                template.convertAndSend("request-queue", requestMap)
            }
        }
    }

    @Scheduled(fixedDelay = 15000)
    fun assignAccounts() {
        if (rabbitAdmin.getQueueInfo("answer-queue").messageCount > 0) {
            val accountsMap = mutableMapOf<Long, Int>()
            repeat(dbServices.getListOfEmptyAccounts().size) {
                val account = template.receiveAndConvert("answer-queue") as Map<Long, Int>
                println("Received account: $account")
                accountsMap.putAll(account)
            }
            accountsMap.forEach { (id, acc) -> dbServices.updateAccount(id, acc) }
            rabbitAdmin.purgeQueue("answer-queue")
        }
    }
}