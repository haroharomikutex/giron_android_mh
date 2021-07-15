package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class CoinEntity (
    override val id: Int,
    @SerializedName("reword_type")
    val rewordType: Int,
    val num: Int
): BaseEntity