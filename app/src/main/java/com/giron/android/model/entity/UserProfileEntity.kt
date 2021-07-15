package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class UserProfileEntity(
    override val id: Int,
    val uuid: String,
    val username: String,
    val profile: String,
    @SerializedName("reword_sum")
    val rewordSum: Int,
    @SerializedName("giron_count")
    val gironCount: Int,
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("emotion_count")
    val emotionCount: Int,
    @SerializedName("updated_at")
    val updatedAt: String
) : BaseEntity