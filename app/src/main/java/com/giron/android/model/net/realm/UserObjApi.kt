@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.realm

import android.util.Log
import com.giron.android.model.dao.UserObj
import com.giron.android.model.entity.UserEntity
import com.giron.android.view.parts.MyUserViewModel
import com.google.gson.Gson
import io.realm.Realm

/**
 * UserObjApi
 */
class UserObjApi: RealmObjApi() {

    /**
     * getData
     *
     * @return MyUserViewModel
     */
    fun getData(): MyUserViewModel? {
        var result: MyUserViewModel? = null

        Realm.getDefaultInstance().use { realm ->
            val userObj = realm.where(UserObj::class.java).findFirst()

            userObj?.also{
                result = MyUserViewModel(it)
            }
        }

        return result
    }

    /**
     * setData
     *
     * @param UserEntity
     */
    fun setData(entity: UserEntity) {
        Log.d(TAG, "start UserObjApi")

        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {r ->
            val user = UserObj()
            user.uuid = entity.uuid
            user.username = entity.username
            user.profile = entity.profile ?: ""
            user.coinSum = entity.coinSum
            user.isShowAdvertise = entity.isShowAdvertise
            user.isRejectMailCoin = entity.isRejectMailCoin
            user.isRejectMailMagazine = entity.isRejectMailMagazine
            user.isRejectMailCampaign = entity.isRejectMailCampaign
            user.advertisementId = entity.advertisementId

            r.insertOrUpdate(user)

            Log.d(TAG, "update user obj ${Gson().toJson(user)}")
        }

        realm.close()
    }

    /**
     * setCoin
     * コイン情報を上書き
     *
     * @param Int coin
     */
    fun setCoin(coin: Int) {
        val realm = Realm.getDefaultInstance()
        val user = realm.where(UserObj::class.java).findFirst()

        user?.run {
            realm.executeTransaction {r ->
                user.coinSum = coin
                r.insertOrUpdate(user)
            }
        }

        realm.close()
    }

    /**
     * setShowAdvertise
     *
     * @param Boolean
     */
    fun setShowAdvertise(value: Boolean) {
        Realm.getDefaultInstance().use {
            val userObj = it.where(UserObj::class.java).findFirst()

            userObj?.let { user ->
                it.executeTransaction { r ->
                    user.isShowAdvertise = value
                    r.insertOrUpdate(user)
                }
            }
        }
    }

    /**
     * setRejectMailCoin
     *
     * @param Boolean
     */
    fun setRejectMailCoin(value: Boolean) {
        Realm.getDefaultInstance().use {
            val userObj = it.where(UserObj::class.java).findFirst()

            userObj?.let { user ->
                it.executeTransaction { r ->
                    user.isRejectMailCoin = value
                    r.insertOrUpdate(user)
                }
            }
        }
    }

    /**
     * setRejectMailMagazine
     *
     * @param Boolean
     */
    fun setRejectMailMagazine(value: Boolean) {
        Realm.getDefaultInstance().use {
            val userObj = it.where(UserObj::class.java).findFirst()

            userObj?.let { user ->
                it.executeTransaction { r ->
                    user.isRejectMailMagazine = value
                    r.insertOrUpdate(user)
                }
            }
        }
    }

    /**
     * setRejectMailCampaign
     *
     * @param Boolean
     */
    fun setRejectMailCampaign(value: Boolean) {
        Realm.getDefaultInstance().use {
            val userObj = it.where(UserObj::class.java).findFirst()

            userObj?.let { user ->
                it.executeTransaction { r ->
                    user.isRejectMailCampaign = value
                    r.insertOrUpdate(user)
                }
            }
        }
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "UserObjApi"
    }
}