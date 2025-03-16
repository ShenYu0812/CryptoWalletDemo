package com.shenyu.server.netty.model.dto

import com.shenyu.server.netty.utils.RatesFluctuationUtil.generateIncreasingRateSequence
import kotlinx.serialization.Serializable

// 分阶梯 0-100个代币汇率，100-1000个代币汇率， 1000-100000个代币汇率， 100000个以上代币汇率
private val fractionations = mutableListOf(0, 100, 1000, 100000)

fun LiveRatesDTO.realTimeChangeRatesFake(): LiveRatesDTO {
    return this@realTimeChangeRatesFake.takeIf { d ->
        d.tiers.isNotEmpty()
    }?.apply dto@{
        this@dto.tiers = this@dto.tiers.map { tier ->
            tier.flatFractionation()
        }
    } ?: this@realTimeChangeRatesFake
}


@Serializable
data class LiveRatesDTO(
    val ok: Boolean,
    val warning: String,
    var tiers: List<TierDTO>
)

@Serializable
data class TierDTO(
    val fromCurrency: String,
    val toCurrency: String,
    var rates: List<RateDTO>,
    var timeStamp: Long
) {
    fun flatFractionation(): TierDTO {
        val seed = rates.maxOf { r -> r.rate.toDoubleOrNull() ?: 0.0 }
        val rateSequence = generateIncreasingRateSequence(seed, fractionations.size).sorted()
        rates = fractionations.zip(rateSequence) { amount, rate ->
            RateDTO("$amount", "$rate")
        }
        timeStamp = System.currentTimeMillis()
        return this@TierDTO
    }
}

@Serializable
data class RateDTO(
    val amount: String,
    val rate: String
)
