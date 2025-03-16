package com.shenyu.server.netty.plugins

import com.shenyu.server.netty.model.request.PublicKeyTransferModel
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("routing")

fun Application.configureRouting() {
    routing {
        post("/api/default") {
            val clientPublicKey = call.receive(PublicKeyTransferModel::class)
            logger.info("received from client:$clientPublicKey")
            val serverPublicKey = generateRSAKeyPair().public.encoded
            val encodedPublicKey = java.util.Base64.getEncoder().encodeToString(serverPublicKey)
            call.respond(PublicKeyTransferModel(publicKey = encodedPublicKey))
        }
        get("/https_link") {
            call.respondText("Hello World!")
        }
    }
}