@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.mypage.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.entity.MyProfileResponseEntity
import com.giron.android.model.net.HttpConstants
import com.giron.android.model.net.UserApiClient
import com.giron.android.model.net.realm.BatchCountObjApi
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.view.parts.BaseViewModelList

/**
 * MyPageViewModel
 */
class MyPageViewModel(val owner: LifecycleOwner) : BaseViewModelList() {
    var imageUserUrl = MutableLiveData(HttpConstants.urlBase() + "users/${UserObjApi().getData()?.uuid}/image_data/")
    var username = MutableLiveData("")
    var gironCount = MutableLiveData("0")
    var commentCount = MutableLiveData("0")
    var emotionCount = MutableLiveData("0")
    var rewordCoin = MutableLiveData("0")
    var informationBatchLabel = MutableLiveData("")
    var visibleInformationBatch = MutableLiveData(false)
    var informationBatchCount = MutableLiveData(0)
    var gCoinBatchCount = MutableLiveData(0)
    var visibleGcoinBatch = MutableLiveData(false)

    /**
     * init
     */
    init {
        informationBatchCount.observe(owner, Observer {
            informationBatchLabel.value = "$it"
            visibleInformationBatch.value = it > 0
        })

        gCoinBatchCount.observe(owner, Observer {
            visibleGcoinBatch.value = it > 0
        })
    }

    /**
     * reload
     */
    fun reload() {
        informationBatchCount.value = BatchCountObjApi().getData(TimeStampObj.Key.INFORMATION)
        gCoinBatchCount.value = BatchCountObjApi().getData(TimeStampObj.Key.CoinByAdvertisement)

        UserApiClient().myProfile({
            set(it)
        },{})
    }

    /**
     * set
     *
     * @param MyProfileResponseEntity
     */
    fun set(entity: MyProfileResponseEntity) {
        username.value = entity.username
        gironCount.value = entity.gironCount.toString()
        commentCount.value = entity.commentCount.toString()
        emotionCount.value = entity.emotionCount.toString()
        rewordCoin.value = entity.rewordSum.toString()
    }
}