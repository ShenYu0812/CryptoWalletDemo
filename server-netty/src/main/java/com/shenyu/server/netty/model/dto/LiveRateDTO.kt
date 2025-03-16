package com.shenyu.server.netty.model.dto

data class LiveRateDTO(
    val fromCurrency: String,
    val toCurrency: String,
    val rates: List<RateDTO>,
    val timeStamp: Long
)

data class RateDTO(
    val amount: String,
    val rate: String
)
