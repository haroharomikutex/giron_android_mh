package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class CommentsEntity (
    val comments: ArrayList<CommentEntity>,
    @SerializedName("max_num")
    val maxNum: Int,
    @SerializedName("max_page")
    val maxPage: Int,
    @SerializedName("current_page")
    val currentPage: Int
)