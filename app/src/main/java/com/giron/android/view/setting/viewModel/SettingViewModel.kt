package com.giron.android.view.setting.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.view.parts.BaseViewModelList

/**
 * SettingViewModel
 */
class SettingViewModel : BaseViewModelList() {
    var isShowAdvertise = MutableLiveData(if (UserObjApi().getData()?.isShowAdvertise != true) "true" else "false")
    var isRejectMailCoin = MutableLiveData(if (UserObjApi().getData()?.isRejectMailCoin != false) "true" else "false")
    var isRejectMailMagazine = MutableLiveData(if (UserObjApi().getData()?.isRejectMailMagazine != false) "true" else "false")
    var isRejectMailCampaign = MutableLiveData(if (UserObjApi().getData()?.isRejectMailCampaign != false) "true" else "false")
}