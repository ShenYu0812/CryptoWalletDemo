package com.shenyu.server.netty

import com.shenyu.server.netty.plugins.configureRouting
import com.shenyu.server.netty.plugins.configureSockets
import com.shenyu.server.netty.plugins.envConfig
import com.shenyu.server.netty.plugins.LifecycleObserver
import com.shenyu.server.netty.plugins.ServerLifecycleListener
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import java.security.Security
import kotlin.time.Duration.Companion.minutes

private val logger = LoggerFactory.getLogger("ktor.application")

private val defaultGracePeriodMillis = 5.minutes.inWholeMilliseconds
private val defaultTimeoutMillis = 15.minutes.inWholeMilliseconds
var serverLifecycleListener: ServerLifecycleListener? = null

private var embeddedServer:
        EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null

fun main() {
    if (Security.getProperty("BC") == null) {
        Security.addProvider(BouncyCastleProvider())
    }
    runCatching {
        embeddedServer(
            factory = Netty,
            environment = applicationEnvironment { log = logger },
            configure = { envConfig() },
            module = Application::module
        ).apply server@{
            LifecycleObserver(this@server, serverLifecycleListener).setupLifecycleHooks()
        }.start(wait = true)
    }.onFailure { e ->
        e.printStackTrace()
    }
}

fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }
    configureRouting()
    configureSockets()
}



fun shotDown(
    gracePeriodMillis: Long = defaultGracePeriodMillis,
    timeoutMillis: Long = defaultTimeoutMillis
) {
    runCatching {
        embeddedServer?.stop(gracePeriodMillis, timeoutMillis)
    }.onFailure { e ->
        e.printStackTrace()
    }

}
