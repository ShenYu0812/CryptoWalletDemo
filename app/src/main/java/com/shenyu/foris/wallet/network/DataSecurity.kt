package com.shenyu.foris.wallet.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.network.tls.certificates.KeyType
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey

import kotlinx.serialization.Serializable


fun generateRSAKeyPair(): KeyPair = runCatching {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(512)
    keyPairGenerator.generateKeyPair()
}.onFailure { e ->
    throw RuntimeException(e)
}.getOrThrow()


suspend fun HttpClient.sendPublicKeyToServer(publicKey: PublicKey, respond: suspend (PublicKeyTransferModel) -> Unit) {
    val publicKeyEncoded = publicKey.encoded
    val publicKeyBase64 = java.util.Base64.getEncoder().encodeToString(publicKeyEncoded)
    val request = PublicKeyTransferModel(publicKey = publicKeyBase64)
    Log.d("session_default", "sendPublicKeyToServer:$request")
    this@sendPublicKeyToServer.post("/api/default") {
        setBody(request)
    }.apply {
        val body = call.response.body<PublicKeyTransferModel>()
        Log.d("session_default", "resp=$body")
        respond.invoke(body)
    }
}

@Serializable
data class PublicKeyTransferModel(
    val keyType: KeyType = KeyType.Client,
    val publicKey: String
)
