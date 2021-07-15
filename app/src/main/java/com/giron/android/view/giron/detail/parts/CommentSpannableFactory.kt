package com.giron.android.view.giron.detail.parts

import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import com.giron.android.util.*
import com.giron.android.view.giron.detail.adapter.GironDetailAdapter

/**
 * CommentSpannableFactory
 * コメントのリンク
 *
 * @param GironDetailAdapter.CommentViewHolder
 */
class CommentSpannableFactory(val holder: GironDetailAdapter.CommentViewHolder): TextViewSpannableFactory(holder.binding.root.context) {

    /**
     * newSpannable
     *
     * @param CharSequence? テキスト文字列
     *
     * @return Spannable
     */
    override fun newSpannable(source: CharSequence?): Spannable {
        this.source = source
        spannable = SpannableString(source)

        val byNum = holder.binding.comment?.comment?.num

        // #[ID]へのリンク
        addLink(GIRON_ID_PATTERN) {
            Log.d(TAG, "touch giron=$it")
            val id = it.replace("#", "").toInt()
            if (id > 0)
                holder.listener.touchGiron(id, byNum = byNum)
        }

        // >>[num]へのリンク
        addLink(COMMENT_NUM_PATTERN) { it ->
            val num = it.replace(">>", "").toIntOrNull()
            Log.d(TAG, "touch comment: $num")
            num?.let {i ->
                holder.listener.touchComment(i, byNum)
            }
        }

        // #[ID]>>numへのリンク
        addLink(GIRON_AND_COMMENT_PATTERN) {
            Log.d(TAG, "touch $it")
            val str = it.split(">>")
            if (str.count() != 2) return@addLink

            val id = str[0].replace("#", "").toIntOrNull()
            val num = str[1].toIntOrNull()
            id?.let {i ->
                holder.listener.touchGiron(i, num, byNum)
            }
        }

        // タグ文字列
        addLink(TAG_PATTERN) {
            Log.d(TAG, "touch tag=$it")
            holder.listener.touchTag(it)
        }

        return spannable
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "CommentSpannableFactory"
    }
}