@file:Suppress("KDocUnresolvedReference")

package com.giron.android.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.widget.TextView
import com.giron.android.extension.setOnSafeClickListener

/**
 * GironSpannableFactory
 *
 * @param TextView
 * @param Context
 * @param fun callback touch Giron
 * @param fun callback touch Tag
 */
class GironSpannableFactory(
    private val textView: TextView,
    private val c: Context,
    private val touchGiron: ((Int, Int?)->Unit)? = null,
    private val touchTag: ((String)->Unit)? = null
) : TextViewSpannableFactory(c) {

    /**
     * newSpannable
     *
     * @param CharSequence?
     *
     * @return Spannable
     */
    override fun newSpannable(source: CharSequence?): Spannable {
        this.source = source
        spannable = SpannableString(source)

        // #[ID]へのリンク
        addLink(GIRON_ID_PATTERN) {
            textView.setOnSafeClickListener {
                Log.d(TAG, "touch giron=$it")
                val id = it.replace("#", "").toInt()
                if (id > 0)
                    touchGiron?.invoke(id, 0)
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
                    touchGiron?.invoke(i, num)
                }
            }
        }

        // タグ文字列
        addLink(TAG_PATTERN) {
            textView.setOnSafeClickListener {
                Log.d(TAG, "touch tag=$it")
                touchTag?.invoke(it)
            }
        }


        return spannable
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "GironSpannableFactory"
    }
}