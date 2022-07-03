package com.monitoring.microservices.service.kotlin.instance.listener

import com.monitoring.microservices.service.kotlin.instance.configuration.InstanceStateConfiguration
import com.monitoring.microservices.service.kotlin.instance.exception.ErrorCode
import com.monitoring.microservices.service.kotlin.instance.exception.InstanceException
import com.monitoring.microservices.service.kotlin.instance.util.Constants
import com.monitoring.microservices.service.kotlin.instance.util.HttpFabric
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component
import java.net.http.HttpResponse

@Component
class CloseContextEventListener: ApplicationListener<ContextClosedEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val httpClient = HttpFabric.httpClient

    override fun onApplicationEvent(event: ContextClosedEvent) {
        logger.info("Context closing process started...")

        try {
            val httpResponse: HttpResponse<String> = httpClient.send(
                HttpFabric.createGetRequest(
                    Constants.REGISTRY_URL +
                        "/instances/status-state?uuid=${InstanceStateConfiguration.instanceUUID}&status=OFFLINE"
                ),
                HttpResponse.BodyHandlers.ofString()
            )

            if (httpResponse.statusCode() == 200)
                return
            else
                logger.error("Registration service respond with status code: ${httpResponse.statusCode()}")
        } catch (exc: Exception) {
            logger.error("Registration service not responding, please try again")
            throw InstanceException(ErrorCode.ERR_INABILITY_TO_COMPLETE_WORK_EXCEPTION)
        }

        logger.info("Instance was successfully disabled")
    }
}
