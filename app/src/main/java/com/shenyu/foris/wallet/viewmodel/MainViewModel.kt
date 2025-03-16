package com.shenyu.foris.wallet.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.shenyu.foris.wallet.model.Currency
import com.shenyu.foris.wallet.model.LiveRatesBean
import com.shenyu.foris.wallet.model.RateWithCurrencyInfo
import com.shenyu.foris.wallet.model.UserTotalBalance
import com.shenyu.foris.wallet.model.WalletBean
import com.shenyu.foris.wallet.model.WalletWithRates
import com.shenyu.foris.wallet.model.amountLevel
import com.shenyu.foris.wallet.model.flatten
import com.shenyu.foris.wallet.model.toMap
import com.shenyu.foris.wallet.network.KtorClient
import com.shenyu.foris.wallet.network.WebSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal


class MainViewModel: ViewModel() {

    private val webSocketClient: WebSocketClient = WebSocketClient(KtorClient.client)

    private val _liveRatesFlow: MutableStateFlow<LiveRatesBean?> = MutableStateFlow(null)
    val liveRatesFlow: StateFlow<LiveRatesBean?> = _liveRatesFlow.asStateFlow()

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())
    val currencies: StateFlow<List<Currency>?> = _currencies.asStateFlow()

    private val _walletBalance = MutableStateFlow<List<WalletBean>>(emptyList())
    val walletBalance: StateFlow<List<WalletBean>> = _walletBalance.asStateFlow()

    val userWalletsTotalBalance: Flow<UserTotalBalance> = combine(walletBalance, liveRatesFlow) { wb, lRates ->
        if (wb.isEmpty() || lRates?.tiers.isNullOrEmpty()) return@combine UserTotalBalance()
        LogUtils.w("wb=${wb.joinToString("\n")}")
        LogUtils.v("lr=${lRates?.tiers?.joinToString("\n")}")

        val wbMap = wb.toMap()
        val walletWithRatesMap = hashMapOf<WalletWithRates.Key, WalletWithRates>()

        LogUtils.i("after wb for loop:${wbMap.entries.joinToString("/n")}")

        if (wbMap.isNotEmpty()) {
            lRates?.tiers?.filter { tier ->
                tier.toCurrency == "USD" && wbMap.containsKey(tier.fromCurrency)
            }?.flatten()?.forEach { rwc ->
                val amount = wbMap[rwc.fromCurrency] ?: return@forEach
                val actualAmountLevel = amount.amountLevel()
                if (actualAmountLevel == rwc.rate.amountLevel()) {
                    val key = WalletWithRates.Key(rwc.fromCurrency, amountLevel = actualAmountLevel)
                    walletWithRatesMap[key] = WalletWithRates(amount, rwc.rate)
                }
            }
        }


        LogUtils.e("after live rates for loop:${walletWithRatesMap.entries.joinToString("/n")}")

        var totalBalance = BigDecimal(0.0)
        walletWithRatesMap.forEach { (_, wwr) ->
            val rate = wwr.rate?.rate?.toDoubleOrNull() ?: return@forEach
            totalBalance += BigDecimal(rate) * BigDecimal(wwr.amount)
        }
        UserTotalBalance(totalBalance = totalBalance)
    }

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    fun launchDefault(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            KtorClient.launchDefault { body ->
                LogUtils.d( "pub:${body.publicKey}")
                getCurrencies()
                postWalletsBalance()
                connectWebSocket(lifecycleOwner)
            }
        }
    }

    private fun connectWebSocket(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            webSocketClient.connectWithFlow().collect { message ->
                // 处理接收到的消息
                handleWebSocketMessage(message)
            }
        }
    }

    fun getCurrencies() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            runCatching {
                KtorClient.getCurrencies().apply {
                    when {
                        code == 0 -> {
                            data?.currencies?.apply cc@{
                                _currencies.emit(this@cc)
                            } ?: _currencies.emit(emptyList())
                        }
                        else -> _error.emit("$message")
                    }
                }
            }.onFailure { e ->
                _error.emit("${e.message}")
            }
        }
    }

    fun postWalletsBalance() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            runCatching {
                KtorClient.postWalletsBalance().apply {
                    LogUtils.e("resp:$data")
                    when {
                        code == 0 -> {
                            data?.wallet?.apply wb@{
                                _walletBalance.emit(this@wb)
                            } ?: _walletBalance.emit(emptyList())
                        }
                        else -> _error.emit("$message")
                    }
                }
            }.onFailure { e ->
                _error.emit("${e.message}")
            }
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