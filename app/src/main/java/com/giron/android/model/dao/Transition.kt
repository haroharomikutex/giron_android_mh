package com.giron.android.model.dao

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.giron.android.view.notifications.viewModel.NoticeType
import com.giron.android.view.notifications.viewModel.NoticeViewModel
import java.io.Serializable

/**
 * Transition
 * 遷移用Object
 */
class Transition(
    var type: TransitionType,
    var gironId: Int,
    var commentNum: Int,
    var url: String? = "",
    var word: String? = ""
): Serializable {
    var noticeId: Int = 0

    /**
     * static
     */
    companion object {
        const val KEY = "Transition"
        const val TAG = "Transition"

        /**
         * fromNotice
         * 通知から遷移用Objを作成する
         *
         * @param notice 通知ViewModel
         */
        fun fromNotice(notice: NoticeViewModel) : Transition {
            var type = when(notice.noticeType) {
                NoticeType.GIRON, NoticeType.COMMENT, NoticeType.REWORD, NoticeType.EMOTION -> TransitionType.GIRON
                NoticeType.COIN -> TransitionType.COIN
                NoticeType.MYPAGE -> TransitionType.MYPAGE
                NoticeType.ADVERTISEMENT -> TransitionType.COIN_AD_HISTORY
                NoticeType.URL -> TransitionType.WEB
                NoticeType.REQUIEDURL -> TransitionType.REQUIRED_WEB
                else -> TransitionType.NONE
            }
            if (type == TransitionType.NONE && notice.entity.giron != null) {
                type = TransitionType.GIRON
            }
            val gironId = notice.entity.giron?.id ?: 0
            val commentNum = notice.entity.comment?.num ?: 0
            val url = notice.entity.url

            return Transition(type, gironId, commentNum, url)
        }

        /**
         * fromIntent
         *
         * @param intent
         *
         * @return intent
         */
        fun fromIntent(intent: Intent): Transition? {
            Log.d(TAG, "check from Intent")
            val transition = Transition(TransitionType.NONE, 0, 0, "")

            intent.data?.let {
                return fromDeepLink(it)
            }

            intent.getStringExtra("giron_id")?.let {
                val id = it.toInt()
                transition.type = TransitionType.GIRON
                transition.gironId = id
                Log.d(TAG, "extra GironID: $id")
            }

            intent.getStringExtra("word")?.let {
                transition.type = TransitionType.SEARCH_WORD
                transition.word = it
                Log.d(TAG, "extra word: $it")
            }

            intent.getStringExtra("notice_id")?.let {
                val id = it.toInt()
                transition.noticeId = id
                Log.d(TAG, "extra NoticeID: $id")
            }

            return transition
        }

        /**
         * fromDeepLink
         *
         * @param uri
         *
         * @return Transition?
         */
        private fun fromDeepLink(uri: Uri): Transition? {
            Log.d(TAG, "fromDeepLink")
            val transition = Transition(TransitionType.NONE, 0, 0, "")

            // ディープリンク解析
            if(uri.toString().contains("share/giron/?")) {
                val arr = uri.toString().split("share/giron/?")
                if (arr.count() != 2) return null

                val params = getParams(arr[1])
                Log.d(TAG, params.toString())
                params["giron_id"]?.let {giron_id ->
                    val id = giron_id.toInt()
                    transition.type = TransitionType.GIRON
                    transition.gironId = id
                    Log.d(TAG, "link gironID: $giron_id")

                    return transition
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