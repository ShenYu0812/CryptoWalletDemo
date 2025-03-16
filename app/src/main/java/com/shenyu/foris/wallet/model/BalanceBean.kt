package com.shenyu.foris.wallet.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BalanceBean(
    val ok: Boolean,
    val warning: String,
    val wallet: List<WalletBean>,
): Parcelable


@Parcelize
data class WalletBean(
    val currency: String,
    val amount: Double
): Parcelable


fun List<WalletBean>.toMap(): Map<String, Double> {
    val map = hashMapOf<String, Double>()
    forEach { (c, a) ->
        if (c.isNotBlank() && a > 0.0) {
            val old = map[c]
            map[c] = (old ?: 0.0) + a
        }
    }
    return map
}
