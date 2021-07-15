package com.giron.android.model.dao

import com.giron.android.model.net.realm.TagObjApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

/**
 * TagObj
 * 最近追加したタグ
 */
open class TagObj: RealmObject() {
    @PrimaryKey
    var id: String = ""

    @Required
    var name: String = ""
    var type: String = ""
    var createdAt: Date = Date()

    /**
     * set
     * タグの追加
     */
    fun set(name: String, type: TagObjApi.TagObjType) {
        id = "${name}_${type.value}"
        this.name = name
        this.type = type.value
    }
}