package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class BatchCountResponseEntity(
    @SerializedName("giron_count")
    val giron: Int,
    @SerializedName("information_count")
    val information: Int,
    @SerializedName("notice_count")
    val notice: Int,
    @SerializedName("reword_count")
    val reword: Int,
    @SerializedName("coin_by_advertisement_count")
    val advertisementCoin: Int
)