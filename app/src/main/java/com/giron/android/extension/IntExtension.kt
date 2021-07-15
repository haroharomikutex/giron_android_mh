package com.giron.android.extension

import android.content.Context
import android.util.DisplayMetrics

/**
 * randomString
 *
 * ランダム文字列の取得
 */
fun Int.randomString(): String {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..this)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
}

/**
 * pixelからdpへの変換
 * @param px
 * @param context
 * @return Float dp
 */
fun Int.convertPx2Dp(context: Context): Float {
    val metrics: DisplayMetrics = context.resources.displayMetrics
    return this / metrics.density
}