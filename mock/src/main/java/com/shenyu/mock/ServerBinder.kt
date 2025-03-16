package com.shenyu.mock

import android.util.Log
import com.shenyu.server.netty.plugins.ServerLifecycleListener
import com.shenyu.server.netty.serverLifecycleListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

class ServerBinder: IMockService.Stub(), ServerLifecycleListener {

    private var lifecycleCallbacks = CopyOnWriteArrayList<ILifecycleCallback>()

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
        serverLifecycleListener = this@ServerBinder
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.w("start_server", "failed:${throwable.stackTraceToString()}")
            throwable.printStackTrace()
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler + SupervisorJob()).launch {
            com.shenyu.server.netty.main()
        }
    }

    override fun stopServer() {
        com.shenyu.server.netty.shotDown()
    }

    override fun registerServerLifecycle(callback: ILifecycleCallback?) {
        lifecycleCallbacks.add(callback)
    }

    override fun unregisterServerLifecycle(callback: ILifecycleCallback?) {
        lifecycleCallbacks.remove(callback)

    }

    override fun onServerStarted() {
        lifecycleCallbacks.forEach { cb ->
            cb.onLifecycleEvent(0)
        }
    }

    override fun onServerReady(ports: List<Int>) {
        lifecycleCallbacks.forEach { cb ->
            cb.onLifecycleEvent(1)
        }
    }

    override fun cleanup() {
        lifecycleCallbacks.forEach { cb ->
            cb.onLifecycleEvent(2)
        }
        serverLifecycleListener = null
    }
}