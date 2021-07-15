package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class AdvertisedCoinsEntity (
    @SerializedName("max_num")
    val maxNum: Int,
    val coins: ArrayList<AdvertisedCoinEntity>
)