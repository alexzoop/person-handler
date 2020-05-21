package org.zoop.personhandler.process

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.zoop.personhandler.broker.AccountClientsRabbit
import org.zoop.personhandler.database.DbServices

@EnableScheduling
@Component
class ScheduledTasks(
        template: RabbitTemplate,
        dbServices: DbServices,
        rabbitAdmin: RabbitAdmin) {

    val accountClientsRabbit = AccountClientsRabbit(template, dbServices, rabbitAdmin)

    @Scheduled(fixedDelay = 5000)
    fun requestAccountsTask() {
        accountClientsRabbit.requestAccounts()
    }

    @RabbitListener(queues = ["answer-queue"])
    fun assignAccounts(message: Map<Long, Int>) {
        accountClientsRabbit.assignAccounts(message)
    }
}



