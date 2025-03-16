package com.shenyu.foris.wallet.model

import android.os.Parcelable
import com.shenyu.foris.wallet.utils.validateNumber
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal


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
): Parcelable {

    fun balance(): BigDecimal = runCatching {
        val vc = currency.validateNumber()
            ?: throw NumberFormatException("$currency is not a valid number")
        (vc * amount).toBigDecimal()
    }.onFailure { e ->
        e.printStackTrace()
    }.getOrElse { BigDecimal.ZERO }
}
