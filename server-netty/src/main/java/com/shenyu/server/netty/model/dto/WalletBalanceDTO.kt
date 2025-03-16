package com.shenyu.server.netty.model.dto

data class WalletBalanceDTO(
    val currency: String,
    val amount: Double
)