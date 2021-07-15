package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class GironEntity (
        override val id: Int,
        val title: String,
        val description: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        val user: UserEntity,
        @SerializedName("comment_count")
        val commentCount: Int,
        @SerializedName("is_emotion")
        val isEmotion: Boolean,
        @SerializedName("all_coin_count")
        val allCoinCount: Int,
        @SerializedName("all_emotion_count")
        val allEmotionCount: Int,
        val batches: ArrayList<Int>,
        @SerializedName("view_count")
        val viewCount: Int,
        @SerializedName("is_emotions")
        val IsEmotions: ArrayList<EmotionEntity>,
        val emotions: ArrayList<EmotionEntity>,
        @SerializedName("last_comment")
        val lastComment: CommentEntity?,
        val comments: ArrayList<CommentEntity>,
        val tags: ArrayList<TagEntity>,
        @SerializedName("max_num")
        var maxNum: Int,
        @SerializedName("max_page")
        var maxPage: Int,
        @SerializedName("current_page")
        var currentPage: Int
) : BaseEntity

