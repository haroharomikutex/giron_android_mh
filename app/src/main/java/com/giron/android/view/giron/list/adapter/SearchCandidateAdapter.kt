@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.giron.android.databinding.ItemSearchCandidateBinding
import com.giron.android.databinding.ItemSearchCandidateHeaderBinding
import com.giron.android.view.giron.list.viewModel.SearchCandidateHeaderViewModel
import com.giron.android.view.giron.list.viewModel.SearchCandidateViewModel
import com.giron.android.view.giron.list.viewModel.SearchCandidatesViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * GironTopAdapter
 *
 * @param LifecycleOwner
 * @param GironTopViewModel
 */
class SearchCandidateAdapter(private var owner: LifecycleOwner, override val model: SearchCandidatesViewModel): BaseViewAdapter(model) {
    /**
     * onCreateViewHolder
     *
     * @param ViewGroup
     * @param Int
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(ViewModelType.fromInt(viewType)) {
            ViewModelType.SearchCandidate -> {
                val binding = ItemSearchCandidateBinding.inflate(inflater, parent, false)
                binding.lifecycleOwner = owner
                SearchCandidateViewHolder(binding, owner)
            }
            else -> {
                val binding = ItemSearchCandidateHeaderBinding.inflate(inflater, parent, false)
                binding.lifecycleOwner = owner
                SearchCandidateHeaderViewHolder(binding, owner)
            }
        }
    }

    /**
     * SearchCandidateHeaderViewHolder
     *
     * @param ItemSearchCandidateHeaderBinding
     * @param LifecycleOwner
     */
    class SearchCandidateHeaderViewHolder(val binding: ItemSearchCandidateHeaderBinding, val owner: LifecycleOwner): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as SearchCandidateHeaderViewModel
            binding.searchCandidate = model
        }
    }

    /**
     * SearchCandidateViewHolder
     *
     * @param ItemSearchCandidateBinding
     * @param LifecycleOwner
     */
    class SearchCandidateViewHolder(val binding: ItemSearchCandidateBinding, val owner: LifecycleOwner): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as SearchCandidateViewModel
            binding.searchCandidate = model
        }
    }
}