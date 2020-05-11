/*package org.zoop.personhandler.broker

import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableRabbit
@Configuration
class RabbitConfiguration {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        return CachingConnectionFactory("localhost")
    }

    @Bean
    fun amqpAdmin(): AmqpAdmin {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory())
        rabbitTemplate.setDefaultReceiveQueue("account-queue")
        rabbitTemplate.setReplyTimeout(60 * 1000.toLong())
        return rabbitTemplate
    }

    @Bean
    fun myQueue(): Queue {
        return Queue("account-queue")
    }
}*/