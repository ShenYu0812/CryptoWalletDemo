package com.shenyu.server.netty.model.dto

import kotlinx.serialization.Serializable


@Serializable
data class WalletBalanceDTO(
    val ok: Boolean,
    val warning: String,
    val wallet: List<WalletDTO>
)


@Serializable
data class WalletDTO(
    val currency: String,
    val amount: Double
)
