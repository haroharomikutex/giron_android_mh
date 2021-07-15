package com.giron.android.model.entity

import com.google.gson.annotations.SerializedName

data class RejectMailCampaignEntity (
    @SerializedName("is_reject_mail_campaign")
    val isRejectMailCampaign: Boolean
)