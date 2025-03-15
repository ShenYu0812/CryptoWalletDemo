package com.shenyu.server.netty.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/https_link") {
            call.respondText("Hello World!")
        }
    }
}