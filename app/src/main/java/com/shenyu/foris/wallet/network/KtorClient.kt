package com.shenyu.foris.wallet.network

import android.util.Log
import com.google.gson.Gson
import com.shenyu.foris.wallet.model.BalanceBean
import com.shenyu.foris.wallet.model.CurrenciesBean
import com.shenyu.foris.wallet.model.LiveRatesBean
import com.shenyu.foris.wallet.model.base.GenericResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import io.ktor.websocket.Frame
import io.ktor.websocket.readText


object KtorClient {
    private val gson = Gson()

    val client = HttpClient(OkHttp) {
        engine {
            config {
                val trustManager = SslSettings.createTrustManager()
                sslSocketFactory(SslSettings.createSSLContext(trustManager).socketFactory, trustManager)
            }
        }
        install(ContentNegotiation) {
            gson()
        }

        install(Logging) {
            level = LogLevel.ALL
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            url {
                host = "127.0.0.1"
                port = 8443
                protocol = URLProtocol.HTTPS
            }
        }
        install(WebSockets)
    }

    suspend fun launchDefault(respond: suspend (PublicKeyTransferModel) -> Unit) {
        val publicKey = generateRSAKeyPair().public
        client.sendPublicKeyToServer(publicKey, respond)
    }

    suspend fun getCurrencies() {
        val call = client.get("/api/currencies")
        val bean = call.body<GenericResponse<CurrenciesBean?>>()
        println("getCurrencies: $bean")
    }

    suspend fun postWalletsBalance() {
        val call = client.post("/api/wallets/balance") {
            // TODO: 构造请求
        }
        val bean = call.body<GenericResponse<BalanceBean?>>()
        println("postBalance: $bean")
    }

    suspend fun connectWebSocket(updateBlocking: suspend (LiveRatesBean?) -> Unit) {
        client.webSocket("wss://127.0.0.1:8443/socket/live_rates") {
            for (frame in incoming) {
                Log.v("rates_update", "received:${frame}")
                (frame as? Frame.Text)?.apply text@{
                    val jsonString = this@text.readText()
                    val updateRates = runCatching {
                        gson.fromJson(jsonString, LiveRatesBean::class.java)
                    }.getOrNull()
                    updateBlocking.invoke(updateRates)
                }
            }
        }
    }

    suspend fun connectTestWebSocket() {
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