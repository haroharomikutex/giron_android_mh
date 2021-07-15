@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.parts

import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import com.giron.android.R
import com.giron.android.util.TAG_PATTERN
import com.giron.android.util.TextViewSpannableFactory
import com.giron.android.view.giron.detail.adapter.GironDetailAdapter

/**
 * GironDetailTagSpannableFactory
 * タグ、及びタグ追加ボタンをリンク化する
 */
class GironDetailTagSpannableFactory(private val holder: GironDetailAdapter.GironsDetailViewHolder) : TextViewSpannableFactory(holder.binding.root.context) {
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
            Log.d(TAG, "touch tag=$it")
            holder.listener.touchTag(it)
        }

        Log.d(TAG, holder.binding.root.context.getString(R.string.add_tag))

        addLink("＋", false) {
            Log.d(TAG, "touch add tag")
            holder.addTag()
        }

        return spannable
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "GironDetailTagSpFact"
    }
}