package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * BatchCountObj
 */
open class BatchCountObj : RealmObject() {
    @PrimaryKey
    var key : String = ""
    var value : Int = 0
}