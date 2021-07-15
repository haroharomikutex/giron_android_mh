package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class UserSimpleEntity(
    val uuid: String,
    val username: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val profile: String
)