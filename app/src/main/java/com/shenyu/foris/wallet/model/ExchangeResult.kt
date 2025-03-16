package com.shenyu.foris.wallet.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExchangeResult(
    val fromCurrency: String,
    val toCurrency: String,
    val exchangeAmount: Double,
    val exchangeResult: Double
): Parcelable

// TODO: tmp data
data class DashboardState(
    val totalUsdBalance: java.math.BigDecimal = java.math.BigDecimal.ZERO,
    val currencies: List<CurrencyWithBalance> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class CurrencyWithBalance(
    val currency: Currency,
    val balance: WalletBalance
)
