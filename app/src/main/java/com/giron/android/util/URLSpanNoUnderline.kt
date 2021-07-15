@file:Suppress("KDocUnresolvedReference")

package com.giron.android.util

import android.text.TextPaint
import android.text.style.URLSpan

/**
 * URLSpanNoUnderline
 * 下線なしurl
 *
 * @param String
 */
class URLSpanNoUnderline(src: String) : URLSpan(src)
{
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}