package com.giron.android.view.giron.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.giron.android.R
import com.giron.android.databinding.ButtonEmotionAddBinding
import com.giron.android.databinding.ButtonEmotionCountBinding
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.giron.detail.viewModel.EmotionButtonsViewModel
import com.giron.android.view.giron.detail.viewModel.EmotionViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

class EmotionButtonsAdapter(val owner: LifecycleOwner, override val model: EmotionButtonsViewModel, val listener: EmotionButtonsListener? = null) : BaseViewAdapter(model) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(ViewModelType.fromInt(viewType)) {
            ViewModelType.EmotionCount -> {
                val binding = DataBindingUtil.inflate<ButtonEmotionCountBinding>(inflater, R.layout.button_emotion_count, parent,false)
                binding.lifecycleOwner = owner
                EmotionsViewHolder(binding)
            }
            ViewModelType.EmotionAdd -> {
                val binding = DataBindingUtil.inflate<ButtonEmotionAddBinding>(inflater, R.layout.button_emotion_add, parent,false)
                binding.lifecycleOwner = owner
                AddEmotionViewHolder(binding, listener)
            }
            else -> {
                super.onCreateViewHolder(parent, viewType)
            }
        }
    }

    class EmotionsViewHolder(val binding: ButtonEmotionCountBinding) : BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            var model = _model as EmotionViewModel
            binding.emotion = model
        }
    }

    class AddEmotionViewHolder(val binding: ButtonEmotionAddBinding, val listener: EmotionButtonsListener?) : BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            itemView.setOnClickListener {
                listener?.openEmotionList(itemView)
            }
        }
    }
}