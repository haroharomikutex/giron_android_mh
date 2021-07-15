package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class SearchWordEntity (
        val word: String,
        @SerializedName("word_count")
        val wordCount: Int
)