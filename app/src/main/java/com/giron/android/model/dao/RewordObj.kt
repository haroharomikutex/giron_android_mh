package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RewordObj : RealmObject() {
    @PrimaryKey
    var id: Int = 0

    var num: Int = 0
    var type: Int = 0
    @Required
    var icon: String = ""
    var resource: Int = 0
}