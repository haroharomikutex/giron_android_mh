package com.giron.android.view.giron.detail.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.giron.android.databinding.FragmentCommentActionBinding
import com.giron.android.view.giron.detail.listener.ActionListener
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList

class ActionAdapter(private val owner: LifecycleOwner, override val model: BaseViewModelList, val listener: ActionListener): BaseViewAdapter(model) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return onCreateViewHolder(parent, viewType)
    }

    class ActionViewHolder(val binding: FragmentCommentActionBinding, val listener: ActionListener): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {

        }
    }
}