package com.shenyu.server.netty.model.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class CurrenciesDTO(
    val currencies: List<CurrencyDTO>,
    val total: Int,
    val ok: Boolean = true
)

@Serializable
data class CurrencyDTO(
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
)