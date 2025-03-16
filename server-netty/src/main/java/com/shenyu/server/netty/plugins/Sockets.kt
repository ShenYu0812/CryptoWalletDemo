package com.shenyu.server.netty.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import com.shenyu.server.netty.services.LiveRatesService
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.isActive
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

fun Application.configureSockets() {
    val logger = LoggerFactory.getLogger("WebSocket")
    val liveRatesService = LiveRatesService()

    val activeSessions = ConcurrentHashMap<String, WebSocketSession>()

    install(WebSockets) {
        pingPeriod = 60.seconds
        timeout = 120.seconds
    }

    routing {
        webSocket("/socket/live-rates") {
            try {
                val sessionId = generateSessionId()
                activeSessions[sessionId] = this

                try {
                    // 收集实时汇率流并发送给客户端
                    liveRatesService.generateLiveRatesFlow().collect { ratesJson ->
                        if (!this.isActive) return@collect

                        runCatching {
                            send(Frame.Text(ratesJson))
                            logger.debug("Sent rates update to session {}", sessionId)
                        }.onFailure { e ->
                            logger.error("Failed to send rates to session $sessionId", e)
                        }
                    }
                } finally {
                    activeSessions.remove(sessionId)
                    logger.info("Session $sessionId closed")
                }
            } catch (e: ClosedReceiveChannelException) {
                logger.info("Client disconnected")
            } catch (e: Exception) {
                logger.error("Error in WebSocket session", e)
            }
        }
    }
}

private fun generateSessionId(): String =
    "session_${System.currentTimeMillis()}_${(1000 .. 9999).random()}"