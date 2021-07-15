package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class UserEntity(
    val uuid: String,
    val username: String,
    val image: String,
    @SerializedName("coin_sum")
    val coinSum: Int,
    val imageUrl: String,
    val updatedAt: String,
    val token: String,
    val profile: String?,
    val gironCount: Int,
    val commentCount: Int,
    val emotionCount: Int,
    val reword_sum: Int,
    @SerializedName("advertisement_id")
    val advertisementId: Int?,
    @SerializedName("is_show_advertise")
    val isShowAdvertise: Boolean,
    @SerializedName("is_reject_mail_coin")
    val isRejectMailCoin: Boolean,
    @SerializedName("is_reject_mail_magagine")
    val isRejectMailMagazine: Boolean,
    @SerializedName("is_reject_mail_campaign")
    val isRejectMailCampaign: Boolean
)