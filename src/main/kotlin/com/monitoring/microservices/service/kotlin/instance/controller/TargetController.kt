package com.monitoring.microservices.service.kotlin.instance.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/targets")
class TargetController {
    @PostMapping()
    fun addTarget(): ResponseEntity<String>? {
        return ResponseEntity<String>("Hello world", HttpStatus.CREATED)
    }
}
