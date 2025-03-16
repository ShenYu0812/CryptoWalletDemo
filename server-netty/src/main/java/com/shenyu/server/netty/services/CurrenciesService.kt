package com.shenyu.server.netty.services


import com.shenyu.server.netty.model.dto.CurrenciesDTO
import com.shenyu.server.netty.model.resp.CurrenciesResponse
import com.shenyu.server.netty.utils.ResourceReader
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("currency_service")

object CurrenciesService {

    fun getCurrencies(): CurrenciesResponse {
        return ResourceReader.readJsonResource<CurrenciesDTO>("data/currencies.json")
            .fold(
                onSuccess = { dto ->
                    when {
                        dto.total <= 0 -> {
                            CurrenciesResponse.Error(code = 70001, message = "货币列表为空")
                        }
                        !dto.ok -> {
                            CurrenciesResponse.Error(code = 70002, message = "获取货币列表失败，未知的错误")
                        }
                        else -> {
                            CurrenciesResponse.Success(dto = dto)
                        }
                    }
                },
                onFailure = { e ->
                    logger.error("获取货币列表失败", e)
                    CurrenciesResponse.Error(
                        code = 70003,
                        message = "获取货币列表失败: ${e.message}"
                    )
                }
            )
    }
}