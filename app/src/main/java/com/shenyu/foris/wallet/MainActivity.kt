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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shenyu.foris.wallet.ui.theme.CryptoWalletTheme
import com.shenyu.foris.wallet.viewmodel.MainViewModel
import com.shenyu.foris.wallet.network.DefaultServerLifecycleCallback
import com.shenyu.foris.wallet.network.OnServerLifecycleEvent
import com.shenyu.mock.IMockService
import com.shenyu.mock.MockService

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
                        modifier = Modifier.fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 21.dp)
                            .padding(top = 80.dp)
                    ) {
                        Greeting(name = "Android")
                        Spacer(modifier = Modifier.height(21.dp))
                        Button(
                            onClick = {
                            },
                            modifier = Modifier.fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Text(
                                text = "test a request",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600,
                                textAlign = TextAlign.Center,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                mainViewModel.testWebsockets()
                            },
                            modifier = Modifier.fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Text(
                                text = "link the task websockets",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
        val serviceIntent = Intent(this, MockService::class.java)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    override fun onServerLifecycleEvent(lifecycleState: Int) {
        when (lifecycleState) {
            1 -> {// ready
                mainViewModel.launchDefault()
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        style = LocalTextStyle.current.copy(
            color = Color(0xFF131717),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            lineHeight = 25.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.W600,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CryptoWalletTheme {
        Greeting("Android")
    }
}