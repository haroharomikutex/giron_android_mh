package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class CreateCommentEntity(
        val result: Boolean,
        @SerializedName("comment_count")
        val num: Int
)