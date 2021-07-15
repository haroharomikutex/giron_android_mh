package com.giron.android.view.giron.detail.viewModel

import com.giron.android.model.entity.EmotionEntity
import com.giron.android.model.entity.EmotionsEntity
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList
import com.giron.android.view.parts.ViewModelType

class EmotionButtonsViewModel(val listener: EmotionButtonsListener? = null, private val isEnableAdd: Boolean = true): BaseViewModelList() {
    fun setItems(entity: ArrayList<EmotionEntity>, isEmotions: ArrayList<EmotionEntity>) {
        val models = createViewModels(listener, entity, isEmotions, isEnableAdd)
        items.clear()
        items.addAll(models)
    }

    fun setItems(entity: EmotionsEntity) {
        val models = createViewModels(listener, entity.emotions, entity.IsEmotions, isEnableAdd)
        items.clear()
        items.addAll(models)
    }

    companion object {
        private const val TAG = "EmotionButtonsViewModel"

        fun createViewModels(listener: EmotionButtonsListener? = null, entity: ArrayList<EmotionEntity>, isEmotions: ArrayList<EmotionEntity>, isEnableAdd: Boolean): ArrayList<BaseViewModel> {
            val isEmotionsMap = isEmotions.map { it.emotionType }
            val emotions: List<BaseViewModel> = entity.map {
                EmotionViewModel(it.emotionType, it.count, isEmotionsMap.contains(it.emotionType), listener)
            }

            val adds = ArrayList(emotions)
            if (isEnableAdd) adds.add(BaseViewModel(ViewModelType.EmotionAdd))

            return adds
        }
    }
}