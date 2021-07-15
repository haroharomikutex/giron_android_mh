package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class CoinResponse  (
    @SerializedName("user_coin")
    val userCoin: Int,
    @SerializedName("target_coin")
    val targetCoin: Int?
)