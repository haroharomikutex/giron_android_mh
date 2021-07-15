package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

/**
 * InformationEntity
 */
data class InformationEntity (
    override val id: Int,
    val title: String,
    val message: String,
    @SerializedName("information_date")
    val date: String,
    @SerializedName("information_type")
    val type: Int,
    val url: String,
    val giron: GironSimpleEntity?,
    val comment: CommentSimpleEntity?
): BaseEntity