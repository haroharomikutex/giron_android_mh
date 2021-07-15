package com.giron.android.model.net.realm

import android.content.Context
import android.util.Log
import com.giron.android.model.dao.RewordObj
import com.giron.android.model.net.RewordApiClient
import com.giron.android.view.giron.detail.viewModel.RewordViewModel
import io.realm.Realm

class RewordObjApi :RealmObjApi() {
    fun getData() : List<RewordViewModel>{
        // get Realm instance
        val realm = Realm.getDefaultInstance()

        // get reword data
        val data = realm.where(RewordObj::class.java).findAll()

        val result = data.map { d -> RewordViewModel(d) }

        // close Realm
        realm.close()

        // return list data
        return result
    }

    fun setData(c: Context, pakageName: String) {
        Log.d(TAG, "start RewordApiClient")

        val realm = Realm.getDefaultInstance()

        RewordApiClient().list({
            realm.executeTransaction {r ->
                it.forEach { entity ->
                    val reword = RewordObj()
                    reword.id = entity.id
                    reword.num = entity.num
                    reword.type = entity.type
                    reword.icon = entity.icon
                    reword.resource = c.resources.getIdentifier("ic_${entity.icon}", "drawable", pakageName)
                    r.insertOrUpdate(reword)

                    Log.d(TAG, "update reword entity ${entity.icon}")
                }
            }

            realm.close()
        }, {})
    }

    /**
     * 定義
     */
    companion object {
        private const val TAG = "RewordObjApi"
    }
}