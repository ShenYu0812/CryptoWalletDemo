package com.shenyu.server.netty.model.dto

data class CurrencyDTO(
    val coinId: String,
    val name: String,
    val symbol: String,
    val tokenDecimal: Int,
    val colorfulImageUrl: String,
    val grayImageUrl: String,
    val code: String
)