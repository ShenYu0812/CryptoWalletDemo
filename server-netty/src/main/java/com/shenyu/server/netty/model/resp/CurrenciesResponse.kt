package com.shenyu.server.netty.model.resp

import com.shenyu.server.netty.model.dto.CurrencyDTO

data class CurrenciesResponse(
    val currencies: List<CurrencyDTO>,
    val total: Int,
    val ok: Boolean
)