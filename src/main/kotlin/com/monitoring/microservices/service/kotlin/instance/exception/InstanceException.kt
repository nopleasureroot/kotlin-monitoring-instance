package com.monitoring.microservices.service.kotlin.instance.exception

open class InstanceException(
    errorCode: ErrorCode,
    ) : RuntimeException(errorCode.name)
