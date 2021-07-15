package com.giron.android.extension

import android.util.Log
import android.view.View
import androidx.core.view.postDelayed
import androidx.databinding.BindingAdapter

/**
 * setOnSafeClickListener
 * Onclick with no continuous taps
 *
 * @param () -> Unit
 */
@BindingAdapter("android:onSafeClick")
fun View.setOnSafeClickListener(callback: () -> Unit) {
    this.setOnClickListener {
        if (it.isClickable) {
            Log.d("setOnSafeClickListener", "callback")
            callback()
        }
        it.isClickable = false
        Log.d("setOnSafeClickListener", "isClickable = false")
        it.postDelayed(300) {
            // 300ms内の連続クリックを無効に
            it.isClickable = true
        }
    }
}

fun View.setOnSafeScroll(callback: () -> Unit) {

    this.setOnScrollChangeListener { _, _, _, _, _ ->
        callback()
    }
}