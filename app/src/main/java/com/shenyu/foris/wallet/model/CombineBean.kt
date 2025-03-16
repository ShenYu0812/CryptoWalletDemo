package com.shenyu.foris.wallet.model

import androidx.annotation.IntDef
import java.math.BigDecimal

data class UserTotalBalance(
    val totalBalance: BigDecimal = BigDecimal(0.0),
    val unit: String = "USD", // e.g. USD "${unit} ${totalBalance}"
)


data class WalletWithRates(
    val amount: Double = 0.0,
    val rate: Rate? = null,
) {
    data class Key(
        val fromCurrency: String,
        val toCurrency: String = "USD",
        // TODO: 可以将Level 作为一个
        // 例如0..100: level = 0; 101..1000: level = 1; 1001..100000: level = 2; 100000+: level = 3
        @AmountLevel
        val amountLevel: Int,
    )
}


data class RateWithCurrencyInfo(
    val rate: Rate,
    val fromCurrency: String,
    val toCurrency: String
)


fun List<LiveRate>.flatten(): List<RateWithCurrencyInfo> {
    return flatMap { liveRate ->
        liveRate.rates.map { rate ->
            RateWithCurrencyInfo(rate, liveRate.fromCurrency, liveRate.toCurrency)
        }
    }
}


@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
@Retention(AnnotationRetention.RUNTIME)
@IntDef(
    AmountLevel.LEVEL_0,
    AmountLevel.LEVEL_1,
    AmountLevel.LEVEL_2,
    AmountLevel.LEVEL_3,
)
annotation class AmountLevel {
    companion object {
        const val LEVEL_0 = 0
        const val LEVEL_1 = 1
        const val LEVEL_2 = 2
        const val LEVEL_3 = 3
    }
}


fun Number.amountLevel(): @AmountLevel Int {
    val amount = toDouble()
    return when {
        amount > 100000 -> AmountLevel.LEVEL_3
        amount > 1000 -> AmountLevel.LEVEL_2
        amount > 100 -> AmountLevel.LEVEL_1
        else -> AmountLevel.LEVEL_0
    }
}
