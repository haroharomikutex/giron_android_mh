package com.giron.android.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AlertDialog
import com.giron.android.R
import com.giron.android.model.entity.ErrorResponse
import java.util.*

fun Context.showAlertDialog(message: String) {
    AlertDialog
            .Builder(this)
            .setMessage(message)
            .show()
}

fun Context.showError(error: ErrorResponse) {
    val message = error.error ?: this.getString(R.string.error_ocuccered)
    showAlertDialog(message)
}

fun Context.createLocalizedContext(locale: Locale): Context {
    val res = resources
    val config = Configuration(res.configuration)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocales(LocaleList(locale))
    } else {
        config.setLocale(locale)
    }
    return createConfigurationContext(config)
}

fun Context.setClipboard(text: String) {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("", text))
}