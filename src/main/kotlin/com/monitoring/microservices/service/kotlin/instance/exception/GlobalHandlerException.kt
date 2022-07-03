package com.monitoring.microservices.service.kotlin.instance.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class GlobalHandlerException: ResponseEntityExceptionHandler() {
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> =
        buildResponseEntity(ServiceRegistryError(HttpStatus.BAD_REQUEST, ex, "Malformed JSON request"))

    private fun buildResponseEntity(serviceRegistryError: ServiceRegistryError): ResponseEntity<Any> =
        ResponseEntity(serviceRegistryError, serviceRegistryError.httpStatus)

    @ExceptionHandler(InstanceException::class)
    protected fun handleInstanceException(instanceException: InstanceException): ResponseEntity<Any> {
        logger.error(instanceException.message)
        return buildResponseEntity(ServiceRegistryError(HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(exception: Exception): ResponseEntity<Any> {
        logger.error(exception.message)
        return buildResponseEntity(ServiceRegistryError(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    internal class ServiceRegistryError(val httpStatus: HttpStatus) {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private var timestamp: LocalDateTime = LocalDateTime.now()
        private var debugMessage: String = ""
        private var message: String = ""

        constructor(httpStatus: HttpStatus, ex: Throwable, message: String) : this(httpStatus) {
            this.message = message
            this.debugMessage = ex.localizedMessage
        }
    }
}
