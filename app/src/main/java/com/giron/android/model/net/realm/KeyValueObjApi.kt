package com.giron.android.model.net.realm

import android.util.Log
import com.giron.android.model.dao.KeyValueObj
import io.realm.Realm

class KeyValueObjApi : RealmObjApi() {

    fun getValue(key: Key): String? {
        val realm = Realm.getDefaultInstance()

        val obj = realm
                .where(KeyValueObj::class.java)
                .equalTo("key", key.value)
                .findFirst()

        val value = obj?.value

        realm.close()

        return value
    }

    fun setValue(key: Key, value: String) {
        val realm = Realm.getDefaultInstance()

        // 保存
        realm.executeTransaction{ r ->
            val keyValue = KeyValueObj()
            keyValue.key = key.value
            keyValue.value = value
            r.insertOrUpdate(keyValue)
            Log.d(TAG, "update or insert key=" + key.value  +", value=$value")
        }

        realm.close()
    }

    /**
     * 定義
     */
    companion object {
        private const val TAG = "KeyValueObjApi"
        private const val GIRON_TOKEN_KEY = "giron_token"
    }
}