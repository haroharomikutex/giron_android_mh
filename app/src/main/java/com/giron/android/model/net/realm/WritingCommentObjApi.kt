@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.realm

import android.util.Log
import com.giron.android.model.dao.WritingCommentObj
import io.realm.Realm

/**
 * WritingCommentObjApi
 * Comment holding class during writing
 */
class WritingCommentObjApi: RealmObjApi() {

    /**
     * getData
     *
     * @param Int GironID
     *
     * @return String comment value
     */
    fun getData(id: Int): String {
        var result = ""

        // get Realm instance
        val realm = Realm.getDefaultInstance()

        val data = realm
                .where(WritingCommentObj::class.java)
                .equalTo("id", id)
                .findFirst()

        // get comment if any
        data?.let { result = data.comment }

        // close Realm
        realm.close()

        Log.d(TAG, "get comment id=$id comment=$result")

        return result
    }

    /**
     * setData
     *
     * @param Int GironID
     * @param String comments in the middle of writing
     */
    fun setData(id: Int, comment: String) {
        // get Realm instance
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction { r ->
            val wComment = WritingCommentObj()
            wComment.id = id
            wComment.comment = comment

            r.insertOrUpdate(wComment)

            Log.d(TAG, "set comment id=$id comment=$comment")
        }

        // close Realm
        realm.close()
    }

    /**
     * static
     */
    companion object {
        const val TAG = "WritingCommentObjApi"
    }
}