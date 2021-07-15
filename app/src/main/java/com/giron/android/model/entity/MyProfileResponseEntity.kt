package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class MyProfileResponseEntity(
    val uuid: String,
    val email: String,
    val username: String,
    val profile: String,
    @SerializedName("coin_sum")
    val coinSum: Int,
    @SerializedName("giron_count")
    val gironCount: Int,
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("emotion_count")
    val emotionCount: Int,
    @SerializedName("reword_sum")
    val rewordSum: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("is_show_advertise")
    val isShowAdvertise: Boolean,
    @SerializedName("is_reject_mail_coin")
    val isRejectMailCoin: Boolean,
    @SerializedName("is_reject_mail_magagine")
    val isRejectMailMagazine: Boolean,
    @SerializedName("is_reject_mail_campaign")
    val isRejectMailCampaign: Boolean
)