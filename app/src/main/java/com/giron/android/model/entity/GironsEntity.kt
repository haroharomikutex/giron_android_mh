package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class GironsEntity (
        val girons: ArrayList<GironEntity>,
        @SerializedName("max_num")
        val maxNum: Int)