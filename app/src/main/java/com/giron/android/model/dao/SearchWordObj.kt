package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

/**
 * SearchWordObj
 * 検索した単語
 */
open class SearchWordObj: RealmObject() {
    @PrimaryKey
    var word: String = ""

    @Required
    var createdAt: Date = Date()
}