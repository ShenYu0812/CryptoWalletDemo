package com.shenyu.foris.wallet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExchangeResponse(
    val ok: Boolean,
    val warning: String,
    val tiers: List<Tier>
): Parcelable


@Parcelize
data class Tier(
    @SerializedName("from_currency")
    val fromCurrency: String,
    @SerializedName("to_currency")
    val toCurrency: String,
    val rates: List<Rate>,
    @SerializedName("time_stamp")
    val timestamp: Long
): Parcelable {

    fun exchanged(exchangeAmount: Double): ExchangeResult? = runCatching tier@{
        val rate = this@tier.rates.lastOrNull()?.validateRate()
            ?: return@tier null
        val exchangeResult = exchangeAmount * rate
        ExchangeResult(fromCurrency, toCurrency, exchangeAmount, exchangeResult)
    }.getOrNull()
}


@Parcelize
data class Rate(
    val amount: String,
    val rate: String
): Parcelable {

    fun validateRate(): Double? = runCatching {
        rate.toDoubleOrNull()
    }.getOrNull()

}
