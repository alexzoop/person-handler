package org.zoop.personhandler.broker

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableRabbit
@Configuration
class RabbitConfiguration {
    @Bean
    fun requestQueue() = Queue("request-queue")

    @Bean
    fun answerQueue() =  Queue("answer-queue")
}