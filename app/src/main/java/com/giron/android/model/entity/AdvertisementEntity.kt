package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class AdvertisementEntity(
    override val id: Int,
    @SerializedName("unit_price")
    val unitPrice: Int,
    val url: String,
    val name: String
): BaseEntity