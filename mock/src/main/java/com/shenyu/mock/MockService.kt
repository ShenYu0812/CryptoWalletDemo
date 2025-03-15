package com.shenyu.mock

import android.app.Service
import android.content.Intent
import android.os.IBinder


class MockService: Service() {

    private val serverBinder = ServerBinder()

    override fun onBind(intent: Intent?): IBinder {
        return serverBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent).apply {
            serverBinder.stopServer()
        }
    }

}