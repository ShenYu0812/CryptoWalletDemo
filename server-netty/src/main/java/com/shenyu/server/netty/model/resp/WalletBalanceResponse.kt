package com.shenyu.server.netty.model.resp

import com.shenyu.server.netty.model.dto.WalletBalanceDTO


sealed class WalletBalanceResponse(
    code: Int = 0,
    message: String? = null,
    data: WalletBalanceDTO?
): BaseResponse<WalletBalanceDTO>(code, message, data) {

    data class Success(
        val dto: WalletBalanceDTO,
    ) : WalletBalanceResponse(data = dto)

    data class Error(
        override var code: Int,
        override var message: String?
    ) : WalletBalanceResponse(code, message, null)
}