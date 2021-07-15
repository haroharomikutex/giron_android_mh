package com.giron.android.model.dao

import com.giron.android.model.net.realm.RealmObjApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class KeyValueObj : RealmObject() {
    /**
     * keyリスト
     */
    enum class Key(val string: String): RealmObjApi.Key {
        GIRON_TOKEN("giron_token"),
        EULA_VERSION("eula_version");

        override val value: String get() = string
    }

    @PrimaryKey
    var key : String? = null
    @Required
    var value = ""
}