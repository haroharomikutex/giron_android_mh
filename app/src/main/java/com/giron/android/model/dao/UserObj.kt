package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserObj : RealmObject() {
    @PrimaryKey
    var uuid: String = ""

    var username: String = ""
    var profile: String = ""
    var coinSum: Int = 0
    var isShowAdvertise = true
    var isRejectMailCoin = true
    var isRejectMailMagazine = true
    var isRejectMailCampaign = true
    var advertisementId: Int? = null
}