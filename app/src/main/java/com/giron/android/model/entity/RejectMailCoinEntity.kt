package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class RejectMailCoinEntity (
    @SerializedName("is_reject_mail_coin")
    val isRejectMailCoin: Boolean
)