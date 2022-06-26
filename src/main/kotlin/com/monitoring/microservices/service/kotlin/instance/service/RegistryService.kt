package com.monitoring.microservices.service.kotlin.instance.service

import com.monitoring.microservices.service.kotlin.instance.model.request.RegistryBody
import com.monitoring.microservices.service.kotlin.instance.util.HttpFabric
import com.monitoring.microservices.service.kotlin.instance.util.Constants
import lombok.extern.slf4j.Slf4j
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.ConnectException
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.net.http.HttpTimeoutException
import javax.annotation.PostConstruct

@Slf4j
@Service
class RegistryService(
    @Value("\${server.port}") val port: Int,
    @Value("\${server.servlet.context-path}") val contextPath: String
    ) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val httpClient: HttpClient = HttpFabric.httpClient

    @PostConstruct
    fun init() {
        logger.info("Registration instance process has been started")

        val response: HttpResponse<String>? = registerInstance(RegistryBody(port, contextPath))

        if (response?.statusCode() == 202) {
            logger.info("REGISTRATION INFO: \nRegistration finished, instance registered with \nID:" +
                " ${JSONObject(response.body()).getString("instanceHash")}, " +
                "\nPORT: $port, \nCONTEXT-PATH: $contextPath"
            )
        } else {
            logger.error("Registration failed with status code: " +
                "${response?.statusCode() ?: "Undefined-Code"}" +
                " and message: ${response?.body() ?: "Undefined-Body"}"
            )
        }
    }

    private fun registerInstance(registryBody: RegistryBody): HttpResponse<String>? {
        return try {
            httpClient.send(
                HttpFabric.createPostRequest(Constants.REGISTRY_URL, JSONObject(registryBody).toString()),
                HttpResponse.BodyHandlers.ofString()
            )
        } catch (exc: ConnectException) {
            exc.printStackTrace()

            null
        } catch (exc: HttpTimeoutException) {
            logger.error("Service-registry not available now")
            exc.printStackTrace()

            null
        }
    }
}
