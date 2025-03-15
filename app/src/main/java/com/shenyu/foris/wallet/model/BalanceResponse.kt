package com.shenyu.foris.wallet.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BalanceResponse(
    val ok: Boolean,
    val warning: String,
    val wallet: List<WalletBalance>,
): Parcelable


@Parcelize
data class WalletBalance(
    val currency: String,
    val amount: Double
): Parcelable
