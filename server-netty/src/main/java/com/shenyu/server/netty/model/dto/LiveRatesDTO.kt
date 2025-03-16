package com.shenyu.server.netty.model.dto

import com.google.gson.annotations.SerializedName
import com.shenyu.server.netty.utils.RatesFluctuationUtil.generateIncreasingRateSequence
import kotlinx.serialization.Serializable

// 分阶梯 0-100个代币汇率，100-1000个代币汇率， 1000-100000个代币汇率， 100000个以上代币汇率
private val fractionation = mutableListOf(0, 100, 1000, 100000)


@Serializable
data class LiveRatesDTO(
    val ok: Boolean,
    val warning: String,
    var tiers: List<TierDTO>
)

@Serializable
data class TierDTO(
    @SerializedName("from_currency")
    val fromCurrency: String,
    @SerializedName("to_currency")
    val toCurrency: String,
    var rates: List<RateDTO>,
    @SerializedName("time_stamp")
    var timeStamp: Long
) {
    fun flatFractionation(): TierDTO {
        val seed = rates.maxOf { r -> r.rate.toDoubleOrNull() ?: 0.0 }
        val rateSequence = generateIncreasingRateSequence(seed, fractionation.size).sorted()
        rates = fractionation.zip(rateSequence) { amount, rate ->
            RateDTO("$amount", "$rate")
        }
        return this@TierDTO
    }
}

@Serializable
data class RateDTO(
    val amount: String,
    val rate: String
)
