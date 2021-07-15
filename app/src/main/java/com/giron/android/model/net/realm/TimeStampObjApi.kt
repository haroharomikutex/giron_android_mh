@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.realm

import com.giron.android.model.dao.TimeStampObj
import com.giron.android.util.toApiFormat
import io.realm.Realm
import java.util.*
import kotlin.collections.HashMap

/**
 * TimeStampObjApi
 */
class TimeStampObjApi: RealmObjApi() {
    /**
     * getData
     *
     * @param TimeStampObj.Key
     * @return Date
     */
    fun getData(key: TimeStampObj.Key): Date? {
        var result: Date?

        Realm.getDefaultInstance().use {
            val timestamp = it.where(TimeStampObj::class.java)
                .equalTo("key", key.value)
                .findFirst()

            result = timestamp?.value
        }

        return result
    }

    /**
     * getAllData
     *
     * @return HashMap<String, String?>
     */
    fun getAllData(): HashMap<String, String?> {
        val result = HashMap<String, String?>()

        Realm.getDefaultInstance().use {
            val array = it.where(TimeStampObj::class.java)
                .findAll()

            array.forEach {timestamp ->
                result[timestamp.key] = timestamp.value.toApiFormat()
            }
        }

        return result
    }

    /**
     * setData
     *
     * @param TimeStampObj.key
     */
    fun setData(key: TimeStampObj.Key) {
        // get Realm instance
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val timestamp = TimeStampObj()
                timestamp.key = key.value
                realm.insertOrUpdate(timestamp)
            }
        }
    }
}