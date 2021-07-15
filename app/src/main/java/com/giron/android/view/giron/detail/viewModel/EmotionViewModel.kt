@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.viewModel

import android.util.Log
import com.giron.android.model.entity.EmotionEntity
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * EmotionViewModel
 *
 * @param Int type
 * @param Int count
 * @param Boolean isOn
 * @param EmotionButtonsListener
 */
class EmotionViewModel(type_value: Int, val count: Int, val isOn: Boolean, val listener: EmotionButtonsListener? = null): BaseViewModel(
    ViewModelType.EmotionCount) {
    constructor(entity: EmotionEntity, listener: EmotionButtonsListener? = null) :
            this(entity.emotionType, entity.count, false, listener)

    val type = EmotionType.fromInt(type_value)
    val print = "${type.icon} $count"

    /**
     * onClick
     */
    fun onClick() {
        Log.d(TAG, "onclick ${type.icon}")

        if (isOn) listener?.removeEmotion(type)
        else listener?.addEmotion(type)
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "EmotionViewModel"
    }
}