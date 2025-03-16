package com.shenyu.foris.wallet.model

import android.os.Parcelable
import com.shenyu.foris.wallet.utils.validateNumber
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal


@Parcelize
data class ExchangeResult(
    val fromCurrency: String,
    val toCurrency: String,
    val exchangeAmount: Double,
): Parcelable {

    fun exchanged(liveRates: LiveRatesBean): BigDecimal? = runCatching {
        liveRates.tiers.find { tier ->
            tier.isValidLiveRate() && tier.fromCurrency == fromCurrency
                    && tier.toCurrency == toCurrency
        }?.rates?.mapNotNull { r ->
            r.takeIf { it.amount.validateNumber() != null }
        }?.sortedBy { r ->
            r.amount.validateNumber()
        }?.findLast { r -> //
            r.amount.validateNumber()?.let { amountFloor ->
                exchangeAmount >= amountFloor // 大于兑换代币下限
            } ?: false
        }?.rate.validateNumber()?.let { rate ->
            BigDecimal(rate) * BigDecimal(exchangeAmount)
        }
    }.getOrNull()
}

