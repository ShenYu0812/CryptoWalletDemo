package com.shenyu.server.netty

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing


fun main() {//: Unit = io.ktor.server.netty.EngineMain.main(args)
    embeddedServer(
        factory = Netty,
        host = "127.0.0.1",
        port = 8081,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    routing {
        get("/hello") {
            call.respondText("Hello, world!")
        }
    }
}