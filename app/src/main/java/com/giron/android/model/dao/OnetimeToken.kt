package com.giron.android.model.dao

import android.content.Intent
import android.net.Uri
import android.util.Log
import java.io.Serializable

class OnetimeToken (
    var token: String
): Serializable {
    /**
     * static
     */
    companion object {
        const val KEY = "OnetimeToken"
        const val TAG = "OnetimeToken"

        /**
         * fromIntent
         *
         * @param intent
         */
        fun fromIntent(intent: Intent): OnetimeToken? {
            Log.d(TAG, "check from intent")

            intent.data?.let{ uri ->
                // ディープリンク解析
                if(uri.toString().contains("requests/onetime/?")) {
                    val arr = uri.toString().split("requests/onetime/?")
                    if (arr.count() != 2) return null

                    val params = getParams(arr[1])
                    Log.d(TAG, params.toString())

                    params["token"]?.let { tokenStr ->
                        Log.d(TAG, "request OnetimeToken: $tokenStr")
                        return OnetimeToken(tokenStr)
                    }
                }
            }

            return null
        }

        /**
         * getParams
         * urlのパラメータをHashMapにして返却する
         *
         * @param value
         */
        private fun getParams(value: String): MutableMap<String, String> {
            val params = mutableMapOf<String, String>()
            value.split('&').map {
                val pair = it.split('=')
                if (pair.count() == 2)
                    params[pair[0]] = pair[1]
            }

            return params
        }
    }
}