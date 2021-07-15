@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.editProfile.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.giron.android.model.entity.MyProfileResponseEntity
import com.giron.android.model.net.UserApiClient
import com.giron.android.view.parts.BaseViewModelList

/**
 * EditProfileViewModel
 */
class EditProfileViewModel: BaseViewModelList() {
    var username = MutableLiveData("")
    var profile = MutableLiveData("")
    var isEdited = MutableLiveData(false)
    var entity: MyProfileResponseEntity? = null

    /**
     * set
     *
     * @param LifecycleOwner
     */
    fun set(owner: LifecycleOwner) {
        isEdited.value = false

        UserApiClient().myProfile({
            entity = it
            username.value = it.username
            profile.value = it.profile

            setObserver(owner)
        }){}
    }

    /**
     * setObserver
     *
     * @param LifecycleOwner
     */
    private fun setObserver(owner: LifecycleOwner) {
        username.observe(owner, Observer {
            if (entity != null)
                if(entity!!.username != it)
                    isEdited.value = true
        })

        profile.observe(owner, Observer {
            if (entity != null)
                if(entity!!.profile != it)
                    isEdited.value = true
        })
    }
}