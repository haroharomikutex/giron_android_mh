package com.giron.android.util

import android.util.Log
import com.giron.android.model.dao.KeyValueObj
import com.giron.android.model.net.realm.RealmObjApi
import com.giron.android.model.net.UserApiClient
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class GironFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * onCreate
     */
    override fun onCreate() {
        super.onCreate()

        // Firebase
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().subscribeToTopic("MAIN")

    }

    /**
     * トークンが切り替わった
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // gironトークンが存在する時のみ
        if (!RealmObjApi().hasObj<KeyValueObj>(KeyValueObj.Key.GIRON_TOKEN))
            return

        UserApiClient().setDeviceToken(token,
            {resultEntity ->
                Log.d(TAG, "result: " + Gson().toJson(resultEntity))
            },
            {throwable ->
                Log.w(TAG, "result: "+ Gson().toJson(throwable))
            }
        )
    }

    /**
     * メッセージを受け取った
     */
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d(TAG, "receive Message with :" + p0.data)
    }

    /**
     * 定義
     */
    companion object {
        private const val TAG = "GironFirebaseMsgService"
    }
}