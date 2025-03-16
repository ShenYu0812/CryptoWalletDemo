package com.shenyu.server.netty.services

import com.shenyu.server.netty.model.dto.WalletBalanceDTO
import com.shenyu.server.netty.model.resp.WalletBalanceResponse
import com.shenyu.server.netty.utils.ResourceReader
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("balance_service")

object BalanceService {

    fun postBalance(): WalletBalanceResponse {
        return ResourceReader.readJsonResource<WalletBalanceDTO>("data/wallet_balance.json")
            .fold(
                onSuccess = { dto ->
                    if (!dto.ok || dto.warning.isNotBlank()) {
                        WalletBalanceResponse.Error(
                            code = 90001,
                            message = "获取钱包余额列表失败: ${dto.warning}"
                        )
                    } else {
                        WalletBalanceResponse.Success(dto = dto)
                    }
                },
                onFailure = { e ->
                    logger.error("获取钱包余额列表失败", e)
                    WalletBalanceResponse.Error(
                        code = 90002,
                        message = "获取钱包余额列表失败: ${e.message}"
                    )
                }
            )
    }
}