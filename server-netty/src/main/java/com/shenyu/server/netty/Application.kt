package com.shenyu.server.netty

import com.shenyu.server.netty.plugins.configureRouting
import com.shenyu.server.netty.plugins.envConfig
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory


fun main() {
    runCatching {
        embeddedServer(
            factory = Netty,
            environment = applicationEnvironment {
                log = LoggerFactory.getLogger("ktor.application")
            },
            configure = { envConfig() },
            module = Application::module
        ).start(wait = true)
    }.onFailure { e ->
        e.printStackTrace()
    }
}

fun Application.module() {
    configureRouting()
}