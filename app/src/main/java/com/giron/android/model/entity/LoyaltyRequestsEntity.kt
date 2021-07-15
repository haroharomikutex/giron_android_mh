package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class LoyaltyRequestsEntity (
    @SerializedName("max_num")
    val maxNum: Int,
    val requests: ArrayList<LoyaltyRequestEntity>
)