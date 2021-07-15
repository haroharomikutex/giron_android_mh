@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.coin.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.AdvertisedCoinEntity
import com.giron.android.util.toDate
import com.giron.android.util.toLabel
import com.giron.android.view.coin.listener.AdvertisedCoinListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * AdvertisedCoinViewModel
 *
 * @param AdvertisedCoinEntity
 */
class AdvertisedCoinViewModel(override val entity: AdvertisedCoinEntity, val listener: AdvertisedCoinListener): BaseViewModel(
    ViewModelType.AdvertisedCoin) {
    override val id = entity.id
    val dateStr = MutableLiveData(entity.createdAt.toDate()?.toLabel())
    val name = MutableLiveData(entity.advertisement.name)
    val numStr = MutableLiveData(entity.num.toString())
    val url = entity.advertisement.url
}