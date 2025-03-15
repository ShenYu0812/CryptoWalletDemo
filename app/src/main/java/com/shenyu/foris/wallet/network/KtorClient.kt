package com.shenyu.foris.wallet.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.websocket.Frame
import io.ktor.websocket.readText

class KtorClient {
    private val client = HttpClient(OkHttp) {
        engine {
            config {
                val trustManager = SslSettings.createTrustManager()
                sslSocketFactory(SslSettings.createSSLContext(trustManager).socketFactory, trustManager)
            }
        }

        install(Logging) {
            level = LogLevel.ALL
        }
        install(WebSockets)
    }

    suspend fun makeHttpRequest() {
        val response = client.get("https://127.0.0.1:8443/https_link") {

        }
        println("HTTP response: $response")
    }

    suspend fun connectWebSocket() {
        client.webSocket("wss://127.0.0.1:8443/tasks") {
            val initialMessage = incoming.receive()
            if (initialMessage is Frame.Text) {
                println("Received from server: ${initialMessage.readText()}")
            }

            (0 until 5).forEach { idx ->
                send(Frame.Text("Hello from client! $idx"))
                val response = incoming.receiveCatching().getOrNull() ?: return@webSocket
                if (response is Frame.Text) {
                    println("Received from server after sent $idx: ${response.readText()}")
                }
            }
        }
    }

    fun close() {
        client.close()
    }
}