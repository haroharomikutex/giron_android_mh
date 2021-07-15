package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class AdvertiseResultEntity (
    @SerializedName("is_show_advertise")
    val isShowAdvertise: Boolean
)