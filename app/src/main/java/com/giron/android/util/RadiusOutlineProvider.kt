package com.giron.android.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * RadiusOutlineProvider
 *
 *
 */
class RadiusOutlineProvider(val radius: Float) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
            0,
            0,
            view.width,
            view.height,
            radius
        )
        view.clipToOutline = true
    }
}