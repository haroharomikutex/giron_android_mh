package com.giron.android.model.dao

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * WritingCommentObj
 */
open class WritingCommentObj: RealmObject() {
    @PrimaryKey
    var id = 0

    @Required
    var comment = ""
}