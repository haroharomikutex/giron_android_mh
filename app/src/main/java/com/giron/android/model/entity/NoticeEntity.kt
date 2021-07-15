package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

/**
 * NoticeEntity
 */
data class NoticeEntity (
    override val id: Int,
    val notice: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("notice_type")
    val noticeType: Int,
    val giron: GironSimpleEntity?,
    val comment: CommentSimpleEntity?,
    val emotion: EmotionEntity?,
    val coin: CoinEntity?,
    val noticer: UserSimpleEntity?,
    val url: String = ""
): BaseEntity
