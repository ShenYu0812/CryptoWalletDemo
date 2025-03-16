package com.shenyu.foris.wallet.network

import com.shenyu.mock.ILifecycleCallback

class DefaultServerLifecycleCallback(private val observer: OnServerLifecycleEvent) : ILifecycleCallback.Stub() {
    override fun onLifecycleEvent(lifecycleState: Int) {
        observer.onServerLifecycleEvent(lifecycleState)
    }
}

fun interface OnServerLifecycleEvent {
    fun onServerLifecycleEvent(lifecycleState: Int)
}
