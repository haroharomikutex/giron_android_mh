package com.giron.android.view.coin.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.extension.locate
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.net.UserApiClient
import com.giron.android.model.net.realm.BatchCountObjApi
import com.giron.android.util.toDate
import com.giron.android.util.toFormat
import com.giron.android.view.parts.BaseViewModelList

class CoinViewModel: BaseViewModelList() {
    val coin = MutableLiveData("0")
    val limitCoin = MutableLiveData("0")
    val limitNextCoin = MutableLiveData("0")
    val advertiseBatch = MutableLiveData(0)

    /**
     *
     */
    fun load() {
        advertiseBatch.value = BatchCountObjApi().getData(TimeStampObj.Key.CoinByAdvertisement)

        UserApiClient().getCoinLimit({
            coin.value = it.coin.toString()

            val limitCoinDate = it.limitDate.toDate()?.toFormat()
            val limitNextCoinDate = it.limitNextDate.toDate()?.toFormat()
            limitCoin.value = "$limitCoinDate ${"limit_coin_month".locate()} ${it.limitCoin}"
            limitNextCoin.value = "$limitNextCoinDate ${"limit_coin_month".locate()} ${it.limitNextCoin}"
        }){}
    }
}