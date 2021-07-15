package com.giron.android.util

import android.R.attr.radius
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * CircleOutlineProvider
 * https://qiita.com/ushi3_jp/items/c568e7be76efafbe7de9
 */
class CircleOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
            0,
            0,
            view.width,
            view.height,
            (view.width / 2).toFloat()
        )
        view.clipToOutline = true
    }
}