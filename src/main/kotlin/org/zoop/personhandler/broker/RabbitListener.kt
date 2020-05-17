package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import kotlin.math.absoluteValue

@Component
class RabbitListener (val template: RabbitTemplate) {

    @RabbitListener(queues = ["request-queue"])
    fun reply(message : Map<Long, String>) {
        print("Incoming request on <account-queue>: setting account for person $message : ")

        val key = message.keys.elementAt(0)
        val answer = mapOf(key to message.values.elementAt(0).hashCode().absoluteValue)

        println(answer)
        template.convertAndSend("answer-queue", answer)

    }

}