package com.shenyu.foris.wallet.utils


fun String?.validateNumber(): Double? = this@validateNumber?.runCatching s@{
    this@s.toDoubleOrNull()
}?.getOrNull()