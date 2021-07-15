package com.giron.android.view.giron.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.giron.android.R
import com.giron.android.databinding.ItemEdittagTagBinding
import com.giron.android.view.giron.detail.viewModel.TagViewModel
import com.giron.android.view.giron.detail.viewModel.TagsViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel

class TagsAdapter(override val model: TagsViewModel) : BaseViewAdapter(model) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemEdittagTagBinding>(inflater, R.layout.item_edittag_tag, parent ,false)
        binding.lifecycleOwner = model.owner
        return TagsViewHolder(binding)
    }

    class TagsViewHolder(val binding: ItemEdittagTagBinding): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as TagViewModel
            binding.tag = model
        }
    }
}