package com.shenyu.server.netty.model.dto

import kotlinx.serialization.Serializable


@Serializable
data class CurrenciesDTO(
    val currencies: List<CurrencyDTO>,
    val total: Int,
    val ok: Boolean = true
)

@Serializable
data class CurrencyDTO(
    val coinId: String,
    val name: String,
    val symbol: String,
    val tokenDecimal: Int,
    val colorfulImageUrl: String,
    val grayImageUrl: String,
    val code: String
)