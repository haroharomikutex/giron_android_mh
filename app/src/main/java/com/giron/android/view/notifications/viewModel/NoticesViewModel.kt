@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.notifications.viewModel

import androidx.lifecycle.LifecycleOwner
import com.giron.android.model.net.NoticeApiClient
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList

/**
 * NoticesViewModel
 */
class NoticesViewModel: BaseViewModelList() {
    lateinit var owner: LifecycleOwner

    /**
     * set
     */
    fun set(owner: LifecycleOwner) {
        this.owner = owner
    }

    /**
     * reset
     */
    fun reset() {
        items.clear()
        getData()
    }

    /**
     * getData
     *
     * @param Int
     */
    fun getData() {
        val offset = items.value?.count() ?: 0
        NoticeApiClient().list(offset, {
            val models = it.map { entity ->
                NoticeViewModel(entity)
            }

            items.addAll(models)
        }, {})
    }
}