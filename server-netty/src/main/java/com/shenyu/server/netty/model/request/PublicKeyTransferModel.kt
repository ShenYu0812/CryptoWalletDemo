package com.shenyu.server.netty.model.request

import io.ktor.network.tls.certificates.KeyType
import kotlinx.serialization.Serializable

@Serializable
data class PublicKeyTransferModel(
    val keyType: KeyType = KeyType.Server,
    val publicKey: String
)