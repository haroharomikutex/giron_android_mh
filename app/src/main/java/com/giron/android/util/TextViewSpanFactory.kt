@file:Suppress("KDocUnresolvedReference")

package com.giron.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.giron.android.R
import java.util.regex.Pattern

/**
 * TextViewSpannableFactory
 * TextViewのカスタムリンク
 *
 * @param Context
 */
open class TextViewSpannableFactory(val context: Context) : Spannable.Factory() {
    private val linkColor = context.getColor(R.color.colorGiron)
    var source: CharSequence? = null
    lateinit var spannable: Spannable

    @SuppressLint("ResourceAsColor")
    fun addLink(pattern: String, underline: Boolean = true, callback: ((match: String) -> Unit)) {
        var s: CharSequence = ""
        source?.let {s = it}

        val regex = Pattern.compile(pattern)
        val matcher = regex.matcher(s)

        while (matcher.find()) {
            // ClickableSpanの中でtouchEventを読んでもerrorが出るため、
            // 予め文字列は取得しておく。
            val matchStr = matcher.group()
            val st = matcher.start()
            val ed = matcher.end()

            spannable.setSpan(object : ClickableSpan() {
                override fun onClick(textView: View) {
                    callback.invoke(matchStr)
                }
            }, st, ed, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            ForegroundColorSpan(linkColor).run {
                spannable.setSpan(this, st, ed, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            if (!underline) {
                spannable.setSpan(URLSpanNoUnderline(matchStr), st, ed, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}