package org.zoop.personhandler.process

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.core.MessageSource
import org.springframework.integration.dsl.*
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.FileWritingMessageHandler
import org.springframework.integration.file.filters.SimplePatternFileListFilter
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.zoop.personhandler.dbentities.DbServices
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@Configuration
class IntegrationConfig (val dbServices: DbServices) {
    val inputDir = "xmlData/input"
    val archiveDir = "xmlData/archive"
    val errorDir = "xmlData/error"

    @Bean
    fun rightXmlMover(): IntegrationFlow {
        return IntegrationFlows.from(sourceDirectory(), Consumer { c: SourcePollingChannelAdapterSpec -> c.poller(Pollers.fixedDelay(1000)) })
                .route<Message<File>, Boolean>({ file -> dbServices.unmarshallAndInsertData(file.payload.path) },
                        { target ->
                            target.channelMapping(true, "successfulChannel")
                                    .channelMapping(false, "unsuccessfulChannel")
                        })
                .get()
    }

    @Bean
    fun successfulChannel(): MessageChannel {
        return MessageChannels.queue().get()
    }

    @Bean
    fun unsuccessfulChannel(): MessageChannel {
        return MessageChannels.queue().get()
    }

    @Bean
    fun rightFileWriter(): IntegrationFlow {
        return IntegrationFlows.from("successfulChannel")
                .bridge { e -> e.poller(Pollers.fixedRate(1, TimeUnit.SECONDS, 0)) }
                .handle(successfulDirectory())
                .get()
    }

    @Bean
    fun wrongFileWriter(): IntegrationFlow {
        return IntegrationFlows.from("unsuccessfulChannel")
                .bridge { e -> e.poller(Pollers.fixedRate(2, TimeUnit.SECONDS, 0)) }
                .handle(unsuccessfulDirectory())
                .get()
    }

    @Bean
    fun sourceDirectory(): MessageSource<File> {
        val messageSource = FileReadingMessageSource()
        messageSource.setDirectory(File(inputDir))
        messageSource.setFilter(SimplePatternFileListFilter("*.xml"))
        return messageSource
    }

    @Bean
    fun successfulDirectory(): MessageHandler {
        val handler = FileWritingMessageHandler(File(archiveDir))
        handler.setExpectReply(false)
        handler.setDeleteSourceFiles(true)
        handler.setAutoCreateDirectory(true)
        return handler
    }

    @Bean
    fun unsuccessfulDirectory(): MessageHandler {
        val handler = FileWritingMessageHandler(File(errorDir))
        handler.setExpectReply(false)
        handler.setDeleteSourceFiles(true)
        handler.setAutoCreateDirectory(true)
        return handler
    }
}