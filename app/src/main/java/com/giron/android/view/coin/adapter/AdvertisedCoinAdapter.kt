@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.coin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giron.android.databinding.ItemAdvertisedCoinBinding
import com.giron.android.model.net.AdvertisementApiClient
import com.giron.android.view.coin.viewModel.AdvertisedCoinViewModel
import com.giron.android.view.coin.viewModel.AdvertisedCoinsViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel

/**
 * AdvertisedCoinAdapter
 *
 * @param AdvertisedCoinsViewModel
 */
class AdvertisedCoinAdapter(override val model: AdvertisedCoinsViewModel): BaseViewAdapter(model) {
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
        val binding = ItemAdvertisedCoinBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = model.owner
        return AdvertisedCoinViewHolder(binding)
    }

    /**
     * LoyaltyHistoryViewHolder
     *
     * @param ItemLoyaltyHistoryBinding
     */
    class AdvertisedCoinViewHolder(val binding: ItemAdvertisedCoinBinding): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as AdvertisedCoinViewModel
            binding.coin = model

            binding.main.setOnClickListener {
                AdvertisementApiClient().clickListAdvertise(model.id, {}) {}
                model.listener.clickedAdvertisedCoin(model.url)
            }
        }
    }
}