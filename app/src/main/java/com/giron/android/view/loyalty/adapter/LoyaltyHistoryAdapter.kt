@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.loyalty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giron.android.databinding.ItemLoyaltyHistoryBinding
import com.giron.android.view.loyalty.viewModel.LoyaltyRequestViewModel
import com.giron.android.view.loyalty.viewModel.LoyaltyRequestsViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel

/**
 * LoyaltyHistoryAdapter
 *
 * @param LoyaltyRequestsViewModel
 */
class LoyaltyHistoryAdapter(override val model: LoyaltyRequestsViewModel): BaseViewAdapter(model) {
    /**
     * onCreateViewHolder
     *
     * @param ViewGroup
     * @param Int
     *
     * @return BaseViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoyaltyHistoryBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = model.owner
        return LoyaltyHistoryViewHolder(binding)
    }

    /**
     * LoyaltyHistoryViewHolder
     *
     * @param ItemLoyaltyHistoryBinding
     */
    class LoyaltyHistoryViewHolder(val binding: ItemLoyaltyHistoryBinding): BaseViewHolder(binding.root) {
        /**
         * set
         *
         * @param BaseViewModel
         */
        override fun set(_model: BaseViewModel) {
            val model = _model as LoyaltyRequestViewModel
            binding.loyalty = model
        }
    }
}