package com.giron.android.view

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.giron.android.R
import io.realm.Realm
import io.realm.RealmConfiguration

class GironApplication : Application() {
    private var mContext: Context? = null
    override fun onCreate() {
        super.onCreate()

        // Realm
        Realm.init(this)

        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

        createNotificationChannel()
        instance = this

        Log.d(TAG, "onCreate")
    }

    private fun createNotificationChannel() {
        // Android 8.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // デフォルトチャンネル
            val name = getString(R.string.main_channel)
            val descriptionText = getString(R.string.main_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    /**
     * 定義
     */
    companion object {
        private const val TAG = "GironApplication"
        private const val CHANNEL_ID = "default"
        lateinit var instance: Application private set
    }

}