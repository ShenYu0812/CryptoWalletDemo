package com.shenyu.server.netty.model.resp

import com.shenyu.server.netty.model.dto.WalletBalanceDTO

data class WalletResponse(
    val ok: Boolean,
    val warning: String,
    val wallet: List<WalletBalanceDTO>
)