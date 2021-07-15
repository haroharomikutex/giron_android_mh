package com.giron.android.model.dao

import io.realm.RealmList
import io.realm.RealmObject

/**
 * CreateGironObj
 */
open class CreateGironObj : RealmObject() {
    var title = ""
    var description = ""
    var tags: RealmList<CreateGironTagObj> = RealmList()
}