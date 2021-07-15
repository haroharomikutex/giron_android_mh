package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class KeyIntObj : RealmObject() {
    enum class Key(val value : String) {
        NOTICE("notice"),
        INFORMATION("information"),
        GIRON("giron"),
        REWORDED("reworded"),
        CoinByAdvertisement("coin_by_advertisement");
    }

    @PrimaryKey
    var key : String = ""
    var value : Int = 0
}