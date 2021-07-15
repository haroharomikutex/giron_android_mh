package com.giron.android.view.giron.detail.listener

import android.view.View
import com.giron.android.view.giron.detail.viewModel.EmotionType

interface EmotionButtonsListener {
    fun addEmotion(type: EmotionType) {}
    fun removeEmotion(type: EmotionType) {}
    fun openEmotionList(view: View) {}
}