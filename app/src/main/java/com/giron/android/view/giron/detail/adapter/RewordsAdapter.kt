package com.giron.android.view.giron.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.giron.android.R
import com.giron.android.databinding.ItemRewordBinding
import com.giron.android.extension.setOnSafeClickListener
import com.giron.android.view.giron.detail.listener.RewordsListener
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList
import com.giron.android.view.giron.detail.viewModel.RewordViewModel

class RewordsAdapter(private val owner: LifecycleOwner, override val model: BaseViewModelList, val listener: RewordsListener) : BaseViewAdapter(model) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemRewordBinding>(inflater, R.layout.item_reword, parent, false)
        binding.lifecycleOwner = owner
        return RewordViewHolder(binding, listener)
    }

    class RewordViewHolder(val binding: ItemRewordBinding, val listener: RewordsListener): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as RewordViewModel
            binding.reword = model

            itemView.setOnSafeClickListener{ listener.reword(model.id) }
        }
    }

}