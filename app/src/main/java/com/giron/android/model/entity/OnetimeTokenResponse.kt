package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class OnetimeTokenResponse(
    val result: Boolean,
    @SerializedName("window_type")
    val windowType: String,
    val message: String
)