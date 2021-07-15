package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

/**
 * AdvertisedCoinEntity
 */
data class AdvertisedCoinEntity (
    override val id: Int,
    val num: Int,
    val advertisement: AdvertisementEntity,
    @SerializedName("created_at")
    val createdAt: String
): BaseEntity