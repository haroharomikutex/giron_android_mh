package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class LoyaltyRequestEntity (
    override val id: Int,
    val num: Int,
    @SerializedName("request_type")
    val type: String,
    val state: Int,
    @SerializedName("created_at")
    val createdAt: String
): BaseEntity