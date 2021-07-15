package com.giron.android.view.profile.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.UserProfileEntity
import com.giron.android.model.net.HttpConstants
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

class ProfileHeaderViewModel(override val entity: UserProfileEntity): BaseViewModel(ViewModelType.UserProfile) {
    var username = MutableLiveData(entity.username)
    var profile = MutableLiveData(entity.profile)
    var imageUserUrl = MutableLiveData(HttpConstants.urlBase() + "users/${entity.uuid}/image_data/")
    var gironCount = MutableLiveData(entity.gironCount)
    var commentCount = MutableLiveData(entity.commentCount)
    var emotionCount = MutableLiveData(entity.emotionCount)
    var rewordCoin = MutableLiveData(entity.rewordSum)
}