package com.giron.android.view.giron.detail.parts

import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import com.giron.android.extension.setOnSafeClickListener
import com.giron.android.util.*
import com.giron.android.view.giron.detail.adapter.GironDetailAdapter

class GironDetailDescriptionSpannableFactory(private val holder: GironDetailAdapter.GironsDetailViewHolder) : TextViewSpannableFactory(holder.binding.root.context) {

    override fun newSpannable(source: CharSequence?): Spannable {
        this.source = source
        spannable = SpannableString(source)


        // #[ID]へのリンク
        addLink(GIRON_ID_PATTERN) {
            holder.binding.description.setOnSafeClickListener {
                Log.d(TAG, "touch giron=$it")
                val id = it.replace("#", "").toInt()
                if (id > 0)
                    holder.listener.touchGiron(id)
            }
        }

        // >>[num]へのリンク
        addLink(COMMENT_NUM_PATTERN) {
            holder.binding.description.setOnSafeClickListener {
                Log.d(TAG, "touch comment=$it")
                val num = it.replace(">>", "").toIntOrNull()
                num?.let {i ->
                    holder.listener.touchComment(i)
                }
            }
        }

        // #[ID]>>numへのリンク
        addLink(GIRON_AND_COMMENT_PATTERN) {
            holder.binding.description.setOnSafeClickListener {
                Log.d(TAG, "touch $it")
                val str = it.split(">>")
                if (str.count() != 2) return@setOnSafeClickListener

                val id = str[0].replace("#", "").toIntOrNull()
                val num = str[1].toIntOrNull()
                id?.let {i ->
                    holder.listener.touchGiron(i, num)
                }
            }
        }

        // タグ文字列
        addLink(TAG_PATTERN) {
            holder.binding.description.setOnSafeClickListener {
                Log.d(TAG, "touch tag=$it")
                holder.listener.touchTag(it)
            }
        }


        return spannable
    }

    companion object {
        private const val TAG = "GironDetailDescSpFact"
    }
}