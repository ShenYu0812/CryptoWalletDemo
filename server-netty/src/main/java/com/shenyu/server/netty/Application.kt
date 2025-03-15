package com.shenyu.server.netty

import com.shenyu.server.netty.plugins.configureRouting
import com.shenyu.server.netty.plugins.configureSockets
import com.shenyu.server.netty.plugins.envConfig
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import java.security.Security

private val logger = LoggerFactory.getLogger("ktor.application")

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
        ).start(wait = true)
    }.onFailure { e ->
        e.printStackTrace()
    }
}

fun Application.module() {
    configureRouting()
    configureSockets()
}


