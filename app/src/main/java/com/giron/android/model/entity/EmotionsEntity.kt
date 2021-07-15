package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class EmotionsEntity (
        val result: Boolean,
        @SerializedName("emotion_count")
        val emotionCount: Int,
        @SerializedName("is_emotions")
        val IsEmotions: ArrayList<EmotionEntity>,
        val emotions: ArrayList<EmotionEntity>
)