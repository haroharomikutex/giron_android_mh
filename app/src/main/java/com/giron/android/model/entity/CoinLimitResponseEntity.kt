package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class CoinLimitResponseEntity (
    val coin: Int,
    @SerializedName("paied_coin")
    val paidCoin: Int,
    @SerializedName("limit_date")
    val limitDate: String,
    @SerializedName("limit_next_date")
    val limitNextDate: String,
    @SerializedName("limit_coin")
    val limitCoin: Int,
    @SerializedName("limit_next_coin")
    val limitNextCoin: Int
)