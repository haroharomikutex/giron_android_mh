package com.giron.android.extension

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView

fun TextView.addHyperLink(pattern: String, callback: ((view: View, match: String) -> Unit)) {
    val t = Spannable.Factory.getInstance().newSpannable(text)
    val matcher = pattern.toRegex().toPattern().matcher(text)
    while (matcher.find()) {
        val st = matcher.start()
        val ed = matcher.end()
        t.setSpan(object : ClickableSpan() {
            override fun onClick(textView: View) {
                callback.invoke(textView, matcher.group())
            }
        }, st, ed, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        t.setSpan(UnderlineSpan(), st, ed, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ForegroundColorSpan(Color.BLUE).run {
            t.setSpan(this, st, ed, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    setText(t, TextView.BufferType.SPANNABLE)
    movementMethod = LinkMovementMethod.getInstance()
}