package com.shenyu.foris.wallet.model.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class GenericResponse<T: Parcelable?>(
    open var code: Int = 0, // 业务异常错误码
    open var message: String? = null, // 业务异常信息
    open var data: T?,
): Parcelable