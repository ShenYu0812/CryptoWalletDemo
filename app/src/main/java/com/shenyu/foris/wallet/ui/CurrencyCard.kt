package com.shenyu.foris.wallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shenyu.foris.wallet.model.Currency
import com.shenyu.foris.wallet.model.WalletBean
import com.shenyu.foris.wallet.model.defaultCurrency
import com.shenyu.foris.wallet.utils.formatWithCommas
import com.shenyu.foris.wallet.utils.formatWithPrecision
import java.math.BigDecimal

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CurrencyCard(
    currency: Currency = defaultCurrency(),

    balance: WalletBean = WalletBean("BTC", 1.4),
    modifier: Modifier = Modifier
) {
//    val usdValue = currency.
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 货币图标
                AsyncImage(
                    model = currency.colorfulImageUrl,
                    contentDescription = currency.name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                // 货币信息
                Column {
                    Text(
                        text = currency.code,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = currency.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // 余额信息
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${BigDecimal(1.45678).formatWithPrecision(10)} ${currency.code}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    // TODO: need calc with rate
                    text = BigDecimal(1234.567809).formatWithCommas(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}