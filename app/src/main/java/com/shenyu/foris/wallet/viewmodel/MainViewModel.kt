package com.shenyu.foris.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.shenyu.foris.wallet.model.LiveRatesBean
import com.shenyu.foris.wallet.network.KtorClient
import com.shenyu.foris.wallet.network.WebSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {

    private val webSocketClient: WebSocketClient = WebSocketClient(KtorClient.client)

    private val _liveRatesFlow: MutableStateFlow<LiveRatesBean?> = MutableStateFlow(null)
    val liveRatesFlow: StateFlow<LiveRatesBean?> = _liveRatesFlow.asStateFlow()

    fun launchDefault(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient.launchDefault { body ->
                Log.d("pub_key_from_server", "pub:${body.publicKey}")
                connectWebSocket(lifecycleOwner)
            }
        }
    }

    private fun connectWebSocket(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            webSocketClient.connectWithFlow().collect { message ->
                LogUtils.json("rates_update", "connectWithFlow received:${message}")
                // 处理接收到的消息
                handleWebSocketMessage(message)
            }
        }
    }

    fun getCurrencies() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient.getCurrencies()
        }
    }

    fun postWalletsBalance() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient.postWalletsBalance()
        }
    }

    fun connectTestWebsockets() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient.connectTestWebSocket()
        }
    }

    private fun handleWebSocketMessage(message: String) {
        viewModelScope.launch {
            runCatching {
                val rates = Gson().fromJson(message, LiveRatesBean::class.java)
                _liveRatesFlow.value = rates
            }.onFailure {  e ->
                e.printStackTrace()
            }
        }
    }
}