@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.parts

import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import com.giron.android.util.TAG_PATTERN
import com.giron.android.util.TextViewSpannableFactory
import com.giron.android.view.giron.detail.listener.TagEditListener

/**
 * TagSpannableFactory
 * タグをリンク化する
 */
class TagSpannableFactory(val listener: TagEditListener): TextViewSpannableFactory(listener.listenerContext()) {
    /**
     * newSpannable
     *
     * @param CharSequence?
     */
    override fun newSpannable(source: CharSequence?): Spannable {
        this.source = source
        spannable = SpannableString(source)

        // タグ文字列をクリック
        addLink(TAG_PATTERN, false) {
            val name = it.removePrefix("#")
            Log.d(TAG, "touch tag=$name")
            listener.touchTag(name)
        }

        return spannable
    }

    companion object {
        private const val TAG = "TagSpannableFactory"
    }
}