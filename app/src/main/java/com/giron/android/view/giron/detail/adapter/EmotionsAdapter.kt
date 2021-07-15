package com.giron.android.view.giron.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.giron.android.R
import com.giron.android.databinding.ButtonEmotionBinding
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList
import com.giron.android.view.giron.detail.viewModel.EmotionViewModel

class EmotionsAdapter(val owner: LifecycleOwner, override val model: BaseViewModelList, val listener: EmotionButtonsListener) : BaseViewAdapter(model) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ButtonEmotionBinding>(inflater, R.layout.button_emotion, parent, false)
        binding.lifecycleOwner = owner
        return EmotionViewHolder(binding, listener)
    }

    class EmotionViewHolder(val binding: ButtonEmotionBinding, val listener: EmotionButtonsListener) :
        BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as EmotionViewModel
            binding.emotion = model

            itemView.setOnClickListener { listener.addEmotion(model.type) }
        }
    }

}