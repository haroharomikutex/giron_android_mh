package com.giron.android.model.net

import com.giron.android.BuildConfig
import com.giron.android.util.*

/**
 * 通信で使用する定数を定義する
 */
class HttpConstants {

    companion object {

        // ========== 通信に関する定数群 =========== //
        /** 通信のタイムアウト秒数 */
        const val CONNECT_TIMEOUT_MS = 120L
        /** 読み取りのタイムアウト秒数 */
        const val READ_TIMEOUT_MS = 120L
        /** 最大リトライ回数 */
        const val RETRY_COUNT = 3L

        // ========== リクエストヘッダー =========== //
        const val HEADER_AUTH = "Authorization"

        // ========== URLのリスト =========== //
        /** ドメイン */
        private const val URL_BASE = "https://giron.biz/"
        private const val URL_BASE_STAGING = "https://giron-staging.appspot.com/"
        fun urlBase() : String {
            when (BuildConfig.BUILD_TYPE) {
                "debug" -> return URL_BASE_LOCAL
                "staging" -> return URL_BASE_STAGING
                "release" -> return URL_BASE
            }
            return URL_BASE_LOCAL
        }

        // TODO: 直書きにする
        /* バージョンチェックAPI */
        const val END_POINT_CHECK_VERSION = "check_version"
    }
}