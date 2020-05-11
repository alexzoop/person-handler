package org.zoop.personhandler.broker

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import kotlin.math.absoluteValue

@Component
class RabbitListener {
    @RabbitListener(queues = ["account-queue"])
    fun reply(message : String) : Int{
        println("Incoming request on <account-queue>: setting account for person $message")
        Thread.sleep(1000)
        return message.hashCode().absoluteValue
    }

}