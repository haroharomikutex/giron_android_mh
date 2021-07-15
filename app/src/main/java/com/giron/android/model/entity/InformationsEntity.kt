package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

/**
 * InformationsEntity
 */
data class InformationsEntity (
    @SerializedName("informations")
    val information: ArrayList<InformationEntity>,
    @SerializedName("max_num")
    val maxNum: Int
)