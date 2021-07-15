package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CreateGironTagObj : RealmObject() {
    @PrimaryKey
    var name = ""
    var isLock = false
}