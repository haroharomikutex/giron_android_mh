package com.giron.android.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CommentEntity(
        override val id: Int,
        val user: UserEntity,
        val comment: String,
        val num: Int,
        @SerializedName("giron_id")
        val gironId: Int,
        val type: Int,
        @SerializedName("is_emotions")
        val IsEmotions: ArrayList<EmotionEntity>,
        val emotions: ArrayList<EmotionEntity>,
        @SerializedName("coin_count")
        val coinCount: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String
): BaseEntity