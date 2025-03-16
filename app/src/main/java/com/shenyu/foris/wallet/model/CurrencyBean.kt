package com.shenyu.foris.wallet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Currency(
    @SerializedName("coin_id")
    val coinId: String,
    val name: String,
    val symbol: String,
    @SerializedName("token_decimal")
    val tokenDecimal: Int,
    @SerializedName("contract_address")
    val contractAddress: String,
    @SerializedName("withdrawal_eta")
    val withdrawalEta: List<String>,
    @SerializedName("colorful_image_url")
    val colorfulImageUrl: String,
    @SerializedName("gray_image_url")
    val grayImageUrl: String,
    @SerializedName("has_deposit_address_tag")
    val hasDepositAddressTag: Boolean,
    @SerializedName("min_balance")
    val minBalance: Int,
    @SerializedName("blockchain_symbol")
    val blockchainSymbol: String,
    @SerializedName("trading_symbol")
    val tradingSymbol: String,
    val code: String,
    val explorer: String,
    @SerializedName("is_erc20")
    val isErc20: Boolean,
    @SerializedName("gas_limit")
    val gasLimit: Int,
    @SerializedName("token_decimal_value")
    val tokenDecimalValue: String,
    @SerializedName("display_decimal")
    val displayDecimal: Int,
    @SerializedName("supports_legacy_address")
    val supportsLegacyAddress: Boolean,
    @SerializedName("deposit_address_tag_name")
    val depositAddressTagName: String,
    @SerializedName("deposit_address_tag_type")
    val depositAddressTagType: String,
    @SerializedName("num_confirmation_required")
    val numConfirmationRequired: Int
): Parcelable

// only for ui preview
fun defaultWalletWithCurrencyInfo(): WalletWithCurrencyInfo =
    WalletWithCurrencyInfo(BigDecimal(12.5566), 1.4,"BTC", defaultCurrency())

fun defaultCurrency(): Currency = Currency(
    "BTC", "Bitcoin", "BTC", 8, "", listOf("30 secs", "2 mins", "30 mins"),
    "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5c1246f55568a400e48ac233/bitcoin.png",
    "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/gray_logo/5c1246f55568a400e48ac233/bitcoin1.png",
    false, 0, "BTC", "BTC", "BTC",
    "https://blockchair.com/bitcoin/transaction/", false, 0, "10000000",
    8, false, "", "", 1
)


@Parcelize
data class CurrenciesBean(
    val currencies: List<Currency>,
    val total: Int,
    val ok: Boolean
): Parcelable
