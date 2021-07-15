@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.profile.viewModel

import androidx.lifecycle.LifecycleOwner
import com.giron.android.model.net.UserApiClient
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList

/**
 * ProfileViewModel
 *
 * @param LifecycleOwner
 */
class ProfileViewModel: BaseViewModelList() {
    lateinit var owner: LifecycleOwner
    lateinit var uuid: String

    /**
     * set
     *
     * @param LifecycleOwner
     * @param String UUID
     */
    fun set(owner: LifecycleOwner, uuid: String) {
        this.owner = owner
        this.uuid = uuid

        UserApiClient().profile(uuid, {
            items.clear()

            val models = arrayListOf<BaseViewModel>()
            models.add(ProfileHeaderViewModel(it))

            items.addAll(models)
        }){}
    }
}