package com.shenyu.foris.wallet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shenyu.foris.wallet.R
import com.shenyu.foris.wallet.model.WalletWithCurrencyInfo
import com.shenyu.foris.wallet.utils.formatWithCommas
import com.shenyu.foris.wallet.viewmodel.MainViewModel


private val defaultIconMap = hashMapOf(
    "BTC" to R.drawable.ic_btc,
    "ETH" to R.drawable.ic_eth,
    "CRO" to R.drawable.ic_mco_round,
    "USDT" to R.drawable.ic_usdt,
    "DAI" to R.drawable.ic_dai,
)


@Composable
fun CurrencyList(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onCurrencyClick: (WalletWithCurrencyInfo) -> Unit = {},
) {
    val userBalanceDetails by mainViewModel.userBalanceDetails.collectAsState(emptyList())

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = userBalanceDetails,
            key = { it.currency?.coinId ?: it.currencyName }
        ) { currencyWithBalance ->
            CurrencyCard(
                onClick = {
                    onCurrencyClick(currencyWithBalance)
                },
                imageUrl = currencyWithBalance.currency?.colorfulImageUrl,
                currencyCode = currencyWithBalance.currency?.name,
                currencyName = currencyWithBalance.currencyName,
                balanceAmount = currencyWithBalance.balanceAmount,
                balanceUsd = currencyWithBalance.balanceUsd.formatWithCommas(),
                defaultImage = defaultIconMap[currencyWithBalance.currencyName] ?: R.drawable.ic_mco,
            )
        }
    }
}