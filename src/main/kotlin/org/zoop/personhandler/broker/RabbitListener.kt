package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import kotlin.math.absoluteValue

@EnableRabbit
@Component
class RabbitListener (val template: RabbitTemplate) {

    @RabbitListener(queues = ["request-queue"])
    fun reply(message : String) {
        println("Incoming request on <account-queue>: setting account for person $message")
        template.convertAndSend("answer-queue", message.hashCode().absoluteValue)
    }

}