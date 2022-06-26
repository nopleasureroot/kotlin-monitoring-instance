package com.monitoring.microservices.service.kotlin.instance.configuration

import com.monitoring.microservices.service.kotlin.instance.model.StatusHolder
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component("HealthCheck")
class HealthIndicatorImpl: HealthIndicator {
    override fun health(): Health {
        val statusHolder: StatusHolder = check()
        if (statusHolder != StatusHolder.SUCCESS) {
            return Health.down()
                .withDetail("With error code", statusHolder).build()
        }

        return Health.up().build()
    }

    fun check(): StatusHolder {
        if (false) {
            return StatusHolder.ERR
        }

        return StatusHolder.SUCCESS
    }
}


