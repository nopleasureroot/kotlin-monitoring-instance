package com.monitoring.microservices.service.kotlin.instance.util

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.util.*

class HttpFabric {
    companion object {
        val httpClient: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build()

        fun createGetRequest(url: String): HttpRequest =
            HttpRequest.newBuilder(URI(url))
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build()

        fun createPostRequest(url: String, body: String?): HttpRequest =
            HttpRequest.newBuilder(URI(url))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(if (Objects.isNull(body)) "" else body))
                .build()
    }
}
