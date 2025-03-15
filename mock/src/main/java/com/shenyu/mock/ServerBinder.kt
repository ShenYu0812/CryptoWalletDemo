package com.shenyu.mock

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ServerBinder: IMockService.Stub() {

    override fun basicTypes(
        anInt: Int,
        aLong: Long,
        aBoolean: Boolean,
        aFloat: Float,
        aDouble: Double,
        aString: String?
    ) {
    }

    override fun startServer() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.w("start_server", "failed:${throwable.stackTraceToString()}")
            throwable.printStackTrace()
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler + SupervisorJob()).launch {
//            com.shenyu.server.cio.main()
            com.shenyu.server.netty.main()
        }
    }

    override fun stopServer() {
//        com.shenyu.server.cio.shotDown()
    }
}