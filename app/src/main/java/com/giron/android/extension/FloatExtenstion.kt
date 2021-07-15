package com.giron.android.extension

import android.content.Context
import android.util.DisplayMetrics


/**
 * dpからpixelへの変換
 * @param dp
 * @param context
 * @return Float pixel
 */
fun Float.convertDp2Px(context: Context): Float {
    val metrics: DisplayMetrics = context.resources.displayMetrics
    return this * metrics.density
}