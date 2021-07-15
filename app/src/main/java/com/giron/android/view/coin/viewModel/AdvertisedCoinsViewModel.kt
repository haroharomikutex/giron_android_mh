@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.coin.viewModel

import androidx.lifecycle.LifecycleOwner
import com.giron.android.model.entity.AdvertisedCoinEntity
import com.giron.android.model.net.UserApiClient
import com.giron.android.view.coin.listener.AdvertisedCoinListener
import com.giron.android.view.parts.BaseViewModelList

/**
 * AdvertisedCoinsViewModel
 */
class AdvertisedCoinsViewModel: BaseViewModelList() {
    lateinit var listener: AdvertisedCoinListener
    lateinit var owner: LifecycleOwner

    /**
     * set
     *
     * @param AdvertisedCoinListener
     * @param LifecycleOwner
     */
    fun set(listener: AdvertisedCoinListener, owner: LifecycleOwner) {
        this.listener = listener
        this.owner = owner

        reload()
    }

    /**
     * load
     */
    fun load() {
        val offset = items.count()
        UserApiClient().advertisedCoin(offset, {
           add(it.coins)
        }){}
    }

    /**
     * reload
     */
    fun reload() {
        items.clear()
        UserApiClient().advertisedCoin(0, {
            add(it.coins)
        }){}
    }

    /**
     * add
     */
    private fun add(array: ArrayList<AdvertisedCoinEntity>) {
        val value = array.map {
            AdvertisedCoinViewModel(it, listener)
        }

        items.addAll(value)
        listener.finish()
    }
}