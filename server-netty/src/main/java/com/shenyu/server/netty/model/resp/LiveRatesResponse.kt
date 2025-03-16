package com.shenyu.server.netty.model.resp

import com.shenyu.server.netty.model.dto.LiveRateDTO


data class LiveRatesResponse(
    val ok: Boolean,
    val warning: String,
    val tiers: List<LiveRateDTO>
)