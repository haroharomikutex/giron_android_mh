package com.giron.android.model.entity

/**
 * BannerEntity
 */
data class BannerEntity (
    override val id: Int,
    val window_type: String,
    val giron_id: Int,
    val value: String,
    val url: String
): BaseEntity