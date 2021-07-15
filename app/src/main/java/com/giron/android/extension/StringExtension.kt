package com.giron.android.extension

import com.giron.android.view.GironApplication
import java.util.*

fun String.locate(locale: Locale = Locale.getDefault()): String {
    val context = GironApplication.instance.createLocalizedContext(locale)
    val id = context.resources.getIdentifier(this, "string", context.packageName)

    return context.getString(id)
}