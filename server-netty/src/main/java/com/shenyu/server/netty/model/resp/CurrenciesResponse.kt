package com.shenyu.server.netty.model.resp

import com.shenyu.server.netty.model.dto.CurrenciesDTO


sealed class CurrenciesResponse(
    code: Int = 0,
    message: String? = null,
    data: CurrenciesDTO?
): BaseResponse<CurrenciesDTO>(code, message, data) {

    data class Success(
        val dto: CurrenciesDTO,
    ) : CurrenciesResponse(data = dto)

    data class Error(
        override var code: Int,
        override var message: String?
    ) : CurrenciesResponse(code, message, null)
}
