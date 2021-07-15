@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.realm

import com.giron.android.model.dao.BatchCountObj
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.entity.BatchCountResponseEntity
import io.realm.Realm

/**
 * BatchCountObjApi
 */
class BatchCountObjApi : RealmObjApi() {
    /**
     * getData
     *
     * @param TimeStampObj.Key
     *
     * @return Int Count
     */
    fun getData(key: TimeStampObj.Key): Int {
        Realm.getDefaultInstance().use {realm ->
            val batch =
                realm.where(BatchCountObj::class.java)
                    .equalTo("key", key.value)
                    .findFirst()

            return batch?.value ?: 0
        }
    }

    /**
     * setData
     *
     * @param BatchCountResponseEntity
     */
    fun setData(entity: BatchCountResponseEntity) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { r ->
                val giron = BatchCountObj()
                giron.key = TimeStampObj.Key.GIRON.value
                giron.value = entity.giron
                r.insertOrUpdate(giron)

                val notice = BatchCountObj()
                notice.key = TimeStampObj.Key.NOTICE.value
                notice.value = entity.notice
                r.insertOrUpdate(notice)

                val information = BatchCountObj()
                information.key = TimeStampObj.Key.INFORMATION.value
                information.value = entity.information
                r.insertOrUpdate(information)

                val reword = BatchCountObj()
                reword.key = TimeStampObj.Key.REWORDED.value
                reword.value = entity.reword
                r.insertOrUpdate(reword)

                val ad = BatchCountObj()
                ad.key = TimeStampObj.Key.CoinByAdvertisement.value
                ad.value = entity.advertisementCoin
                r.insertOrUpdate(ad)
            }

        }
    }
}