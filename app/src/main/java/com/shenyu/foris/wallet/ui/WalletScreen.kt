package com.shenyu.foris.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.shenyu.foris.wallet.model.UserTotalBalance
import com.shenyu.foris.wallet.utils.formatWithCommas
import com.shenyu.foris.wallet.viewmodel.MainViewModel


//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val totalBalance by viewModel.userWalletsTotalBalance.collectAsState(UserTotalBalance())
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BalanceHeader(
            totalBalance = totalBalance.totalBalance.formatWithCommas()
        )
    }
}