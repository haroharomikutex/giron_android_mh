package com.giron.android.util

import com.giron.android.view.GironApplication
import com.giron.android.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZZZZ"): Date? {
    val sdFormat = try {
        SimpleDateFormat(pattern, Locale.getDefault())
    } catch (e: IllegalArgumentException) {
        null
    }

    return sdFormat?.let {
        try {
            it.parse(this)
        } catch (e: ParseException){
            null
        }
    }
}

fun Date.toApiFormat(): String {
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return df.format(this)
}

fun Date.toFormat(str: String = "yyyy-MM-dd"): String {
    val df = SimpleDateFormat(str, Locale.getDefault())
    return df.format(this)
}

fun Date.toLabel(): String {
    // 差分の秒数を取得
    val interval = (Date().time - this.time) / 1000

    val context = GironApplication.instance

    if (interval < 60) {
        return "$interval${context.getString(R.string.second_ago)}"
    }
    val minutes = interval / 60
    if (minutes < 60) {
        return "$minutes${context.getString(R.string.minutes_ago)}"
    }
    val hours = minutes / 60
    if (hours < 24) {
        return "$hours${context.getString(R.string.hours_ago)}"
    } else if (hours < 48) {
        return context.getString(R.string.yesterday)
    }

    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return df.format(this)
}

fun Date.isSinceYesterday(): Boolean {
    // 差分の秒数を取得
    val interval = (Date().time - this.time) / 1000

    return interval < 60 * 60 * 24
}