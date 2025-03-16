package com.shenyu.foris.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.shenyu.foris.wallet.R
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

        Box(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .background(color = colorResource(R.color.color_ff0a1f3d)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            ) {
                CurrencyList(viewModel) { _ ->

                }
            }
        }


    }
}