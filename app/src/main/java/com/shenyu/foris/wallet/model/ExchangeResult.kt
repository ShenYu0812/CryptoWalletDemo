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
