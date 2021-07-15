package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * TimeStampObj
 */
open class TimeStampObj: RealmObject() {

    enum class Key(val value : String) {
        NOTICE("notice"),
        INFORMATION("information"),
        GIRON("giron"),
        REWORDED("reworded"),
        CoinByAdvertisement("coin_by_advertisement");
    }

    @PrimaryKey
    var key : String = ""
    var value : Date = Date()
}