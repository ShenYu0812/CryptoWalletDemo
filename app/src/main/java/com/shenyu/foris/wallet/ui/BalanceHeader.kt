package com.shenyu.foris.wallet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shenyu.foris.wallet.R
import com.shenyu.foris.wallet.utils.formatWithCommas
import java.math.BigDecimal


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BalanceHeader(
    totalBalance: String = "36.68",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.color_ff0a1f3d))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.height(38.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_mco),
                modifier = Modifier.size(38.dp),
                contentDescription = "icon mco"
            )
            Text(
                text = stringResource(R.string.str_crypto_com),
                style = LocalTextStyle.current
                    .copy(
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 40.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    ),
            )

            Spacer(modifier = Modifier.width(6.dp))
            Spacer(
                modifier = Modifier.width(1.dp)
                    .height(28.dp)
                    .background(color = Color.White)
            )
            Spacer(modifier = Modifier.width(6.dp))


            Column {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(R.string.str_wefi_wallet),
                    style = LocalTextStyle.current
                        .copy(
                            color = colorResource(R.color.color_b6b8ba),
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Center,
                        ),
                )
            }

        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$",
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        color = colorResource(R.color.color_b6b8ba),
                        fontSize = 22.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold
                    ),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = totalBalance,
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                    ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.str_usd),
                style = MaterialTheme.typography.titleMedium
                    .copy(
                        color = colorResource(R.color.color_b6b8ba),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                    ),
            )
        }
    }
}