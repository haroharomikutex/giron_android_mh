package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class EmotionEntity(
        @SerializedName("emotion_type")
        val emotionType: Int,
        val count: Int
)