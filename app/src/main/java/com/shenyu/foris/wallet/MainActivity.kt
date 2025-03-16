package com.shenyu.foris.wallet

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.shenyu.foris.wallet.ui.theme.CryptoWalletTheme
import com.shenyu.foris.wallet.viewmodel.MainViewModel
import com.shenyu.foris.wallet.network.DefaultServerLifecycleCallback
import com.shenyu.foris.wallet.network.OnServerLifecycleEvent
import com.shenyu.foris.wallet.ui.WalletScreen
import com.shenyu.mock.IMockService
import com.shenyu.mock.MockService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), OnServerLifecycleEvent {

    private val mainViewModel by viewModels<MainViewModel>()
    private var mockServiceProxy: IMockService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mockServiceProxy = IMockService.Stub.asInterface(service)
            mockServiceProxy?.registerServerLifecycle(
                DefaultServerLifecycleCallback(this@MainActivity)
            )
            mockServiceProxy?.startServer()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mockServiceProxy?.stopServer()
            mockServiceProxy = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoWalletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        WalletScreen(
                            viewModel = mainViewModel
                        )
                    }
                }
            }
        }
        val serviceIntent = Intent(this, MockService::class.java)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            mainViewModel.liveRatesFlow.collectLatest { rates ->
                LogUtils.json("rates_update", rates)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    override fun onServerLifecycleEvent(lifecycleState: Int) {
        when (lifecycleState) {
            1 -> {// ready
                mainViewModel.launchDefault(this@MainActivity)
            }
        }
    }
}
