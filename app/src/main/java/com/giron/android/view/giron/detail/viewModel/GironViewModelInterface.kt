package com.giron.android.view.giron.detail.viewModel

import com.giron.android.model.entity.EmotionEntity

interface GironViewModelInterface {
    fun createEmotions(emotions: ArrayList<EmotionEntity>, isEmotions: ArrayList<EmotionEntity>) : ArrayList<EmotionViewModel> {
        val types = isEmotions.map { it.emotionType }
        val result = emotions.map {
            EmotionViewModel(it.emotionType, it.count, types.contains(it.emotionType))
        }
        return ArrayList(result)
    }
}