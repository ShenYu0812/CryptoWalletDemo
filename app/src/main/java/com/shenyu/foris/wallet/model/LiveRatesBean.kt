package com.shenyu.foris.wallet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

private const val VALID_TIME_INTERVAL = 5 * 60 //seconds

@Parcelize
data class LiveRatesBean(
    val ok: Boolean,
    val warning: String,
    val tiers: List<LiveRate>
): Parcelable


@Parcelize
data class LiveRate(
    @SerializedName("from_currency")
    val fromCurrency: String,
    @SerializedName("to_currency")
    val toCurrency: String,
    val rates: List<Rate>,
    @SerializedName("time_stamp")
    val timestamp: Long
): Parcelable {

    fun isValidLiveRate(): Boolean {
        val timeInterval = ((System.currentTimeMillis() / 1000) - timestamp)
        return timeInterval in 0..VALID_TIME_INTERVAL
    }
}


@Parcelize
data class Rate(
    val amount: String,
    val rate: String,
): Parcelable

fun Rate.amountLevel(): @AmountLevel Int {
    return when (amount) {
        "0" -> AmountLevel.LEVEL_0
        "100" -> AmountLevel.LEVEL_1
        "1000" -> AmountLevel.LEVEL_2
        "100000" -> AmountLevel.LEVEL_3
        else -> AmountLevel.LEVEL_2
    }
}