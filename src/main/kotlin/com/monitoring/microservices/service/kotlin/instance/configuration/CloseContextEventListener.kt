package com.monitoring.microservices.service.kotlin.instance.configuration

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component

@Component
class CloseContextEventListener(
): ApplicationListener<ContextClosedEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: ContextClosedEvent) {
        TODO("Not yet implemented")
    }
}
