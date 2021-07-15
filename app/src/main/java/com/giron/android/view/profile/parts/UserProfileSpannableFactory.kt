package com.giron.android.view.profile.parts

import android.content.Context
import android.widget.TextView
import com.giron.android.view.profile.listener.UserProfileListener
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import com.giron.android.extension.setOnSafeClickListener
import com.giron.android.util.*

class UserProfileSpannableFactory(private val textView: TextView, private val listener: UserProfileListener, private val c: Context) : TextViewSpannableFactory(c) {

    override fun newSpannable(source: CharSequence?): Spannable {
        this.source = source
        spannable = SpannableString(source)


        // #[ID]へのリンク
        addLink(GIRON_ID_PATTERN) {
            textView.setOnSafeClickListener {
                Log.d(TAG, "touch giron=$it")
                val id = it.replace("#", "").toInt()
                if (id > 0)
                    listener.touchGiron(id)
            }
        }

        // #[ID]>>numへのリンク
        addLink(GIRON_AND_COMMENT_PATTERN) {
            textView.setOnSafeClickListener {
                Log.d(TAG, "touch $it")
                val str = it.split(">>")
                if (str.count() != 2) return@setOnSafeClickListener

                val id = str[0].replace("#", "").toIntOrNull()
                val num = str[1].toIntOrNull()
                id?.let {i ->
                    listener.touchGiron(i, num)
                }
            }
        }

        // タグ文字列
        addLink(TAG_PATTERN) {
            textView.setOnSafeClickListener {
                Log.d(TAG, "touch tag=$it")
                listener.touchTag(it)
            }
        }


        return spannable
    }

    companion object {
        private const val TAG = "ProfileSpFact"
    }
}