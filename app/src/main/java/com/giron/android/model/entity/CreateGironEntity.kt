package com.giron.android.model.entity

/**
 * CreateGironEntity
 */
data class CreateGironEntity(
    val title: String,
    val description: String,
    val tags: List<TagEditEntity>
)