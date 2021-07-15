package com.giron.android.model.net.realm

import io.realm.Realm
import io.realm.RealmObject

/**
 * keyがString限定
 */
open class RealmObjApi {
    /**
     * keyの定義クラス
     */
    interface Key {
        val value: String get() = ""
    }

    inline fun <reified T: RealmObject> getObjByRealm(realm: Realm, key: Key): T? {
        return realm.where(T::class.java)
                .equalTo("key", key.value)
                .findFirst()
    }

    inline fun <reified T: RealmObject> hasObj(key: Key): Boolean {
        val realm = Realm.getDefaultInstance()

        val result = realm.where(T::class.java)
                .equalTo("key", key.value)
                .findFirst()

        realm.close()

        return result != null
    }
}