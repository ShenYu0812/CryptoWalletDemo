package com.shenyu.server.netty.model.resp

import kotlinx.serialization.Serializable


@Serializable
open class BaseResponse<T>(
    open var code: Int = 0,
    open var message: String? = null,
    open var data: T?,
)