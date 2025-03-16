package com.shenyu.foris.wallet.ui

import androidx.annotation.DrawableRes
import coil.request.ImageRequest
import coil.size.Scale
import com.shenyu.foris.wallet.R

fun ImageRequest.Builder.applyDefaults(key: String, @DrawableRes default: Int) = this.apply {
    crossfade(true)
    placeholder(R.drawable.ic_currency_loading)
    error(default)
    fallback(default)
    scale(Scale.FILL)
    memoryCacheKey("${key}_memory")
    diskCacheKey("${key}_disk")
}