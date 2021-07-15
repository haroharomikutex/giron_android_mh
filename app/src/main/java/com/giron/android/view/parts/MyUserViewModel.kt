package com.giron.android.view.parts

import androidx.lifecycle.ViewModel
import com.giron.android.model.dao.UserObj

class MyUserViewModel(val obj: UserObj): ViewModel() {
    var coinSum = obj.coinSum.toString()
    var uuid = obj.uuid
    var isShowAdvertise = obj.isShowAdvertise
    var isRejectMailCoin = obj.isRejectMailCoin
    var isRejectMailMagazine = obj.isRejectMailMagazine
    var isRejectMailCampaign = obj.isRejectMailCampaign
    var advertiseId = obj.advertisementId
}