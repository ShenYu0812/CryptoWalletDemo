package com.shenyu.foris.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenyu.foris.wallet.network.KtorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {

    fun launchDefault() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient().launchDefault { body ->
                Log.d("pub_key_from_server", "pub:${body.publicKey}")
            }
        }
    }

    fun testLocalHttpRequest() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient().makeHttpRequest()
        }
    }

    fun testWebsockets() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient().connectWebSocket()
        }
    }
}