package com.shenyu.foris.wallet.utils

import com.blankj.utilcode.util.StringUtils
import java.math.BigDecimal


fun String?.validateNumber(): Double? = this@validateNumber?.runCatching s@{
    this@s.toDoubleOrNull()
}?.getOrNull()

fun BigDecimal.formatWithCommas(): String {
    return StringUtils.format("%,.2f", this@formatWithCommas)
}

fun BigDecimal.formatWithPrecision(precision: Int): String {
    return StringUtils.format("%.${precision}f", this@formatWithPrecision)
}
