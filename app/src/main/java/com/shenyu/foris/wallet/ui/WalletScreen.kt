package com.shenyu.foris.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BalanceHeader(
            //totalBalance = "100.00",
        )
    }
}