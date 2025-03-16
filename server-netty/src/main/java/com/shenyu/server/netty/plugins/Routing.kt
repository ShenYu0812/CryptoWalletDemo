package com.shenyu.server.netty.plugins

import com.shenyu.server.netty.model.request.PublicKeyTransferModel
import com.shenyu.server.netty.model.resp.CurrenciesResponse
import com.shenyu.server.netty.model.resp.WalletBalanceResponse
import com.shenyu.server.netty.services.BalanceService
import com.shenyu.server.netty.services.CurrenciesService
import io.ktor.http.HttpStatusCode
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


        routing {
            get("/api/currencies") {
                when (val response = CurrenciesService.getCurrencies()) {
                    is CurrenciesResponse.Success -> {
                        call.respond(HttpStatusCode.OK, response)
                    }
                    is CurrenciesResponse.Error -> {
                        call.respond(
                            status = when (response.code) {
                                404 -> HttpStatusCode.NotFound
                                500 -> HttpStatusCode.InternalServerError
                                else -> HttpStatusCode.BadRequest
                            },
                            message = response
                        )
                    }
                }
            }
        }

        routing {
            post("/api/wallets/balance") {
                when (val response = BalanceService.postBalance()) {
                    is WalletBalanceResponse.Success -> {
                        call.respond(HttpStatusCode.OK, response)
                    }
                    is WalletBalanceResponse.Error -> {
                        call.respond(
                            status = when (response.code) {
                                404 -> HttpStatusCode.NotFound
                                500 -> HttpStatusCode.InternalServerError
                                else -> HttpStatusCode.BadRequest
                            },
                            message = response
                        )
                    }
                }
            }
        }

        // only for test
        get("/https_link") {
            call.respondText("Hello World!")
        }
    }
}