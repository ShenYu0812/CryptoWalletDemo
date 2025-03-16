package com.shenyu.foris.wallet.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.io.Closeable

class WebSocketClient(
    private val client: HttpClient,
    private val url: String = "wss://127.0.0.1:8443/socket/live-rates"
) : Closeable {
    private val logger = LoggerFactory.getLogger("live_rates_socket")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // 用于发送消息的Channel
    private val messageChannel = Channel<String>()
    // 用于广播接收到的消息
    private val _receivedMessages = MutableSharedFlow<String>()

    // 连接状态
    private var session: DefaultClientWebSocketSession? = null
    private var isConnected = false

    /*** 使用Flow接收消息*/
    fun connectWithFlow(): Flow<String> {
        scope.launch {
            try {
                client.webSocket(url) {
                    session = this
                    isConnected = true
                    logger.info("WebSocket连接成功")

                    // 启动发送消息的协程
                    launch { handleOutgoing() }
                    // 处理接收到的消息
                    handleIncoming()
                }
            } catch (e: Exception) {
                logger.error("WebSocket连接失败", e)
                reconnect()
            }
        }
        return _receivedMessages
    }

    private suspend fun DefaultClientWebSocketSession.handleIncoming() {
        try {
            while (isActive) {
                val frame = incoming.receive()
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        _receivedMessages.emit(text)
                    }
                    is Frame.Close -> {
                        isConnected = false
                        break
                    }
                    else -> { /* TODO */ }
                }
            }
        } catch (e: Exception) {
            logger.error("接收消息失败", e)
            reconnect()
        }
    }

    private suspend fun DefaultClientWebSocketSession.handleOutgoing() {
        try {
            for (message in messageChannel) {
                send(Frame.Text(message))
            }
        } catch (e: Exception) {
            logger.error("发送消息失败", e)
        }
    }

    // 重连
    private fun reconnect() {
        scope.launch {
            if (!isConnected) {
                logger.info("尝试重新连接...")
                connectWithFlow()
            }
        }
    }

    override fun close() {
        scope.launch {
            session?.close()
        }
        messageChannel.close()
        scope.cancel()
    }
}