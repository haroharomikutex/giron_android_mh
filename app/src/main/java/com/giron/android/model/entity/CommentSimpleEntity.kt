package com.giron.android.model.entity

data class CommentSimpleEntity (
    override val id: Int,
    val num: Int,
    val comment: String
): BaseEntity