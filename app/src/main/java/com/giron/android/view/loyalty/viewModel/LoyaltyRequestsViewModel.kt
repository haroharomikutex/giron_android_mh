package com.giron.android.view.loyalty.viewModel

import androidx.lifecycle.LifecycleOwner
import com.giron.android.model.entity.LoyaltyRequestEntity
import com.giron.android.model.net.UserApiClient
import com.giron.android.view.loyalty.listener.LoyaltyListener
import com.giron.android.view.parts.BaseViewModelList

/**
 * LoyaltyRequestsViewModel
 */
class LoyaltyRequestsViewModel: BaseViewModelList() {
    private lateinit var listener: LoyaltyListener
    lateinit var owner: LifecycleOwner

    fun set(listener: LoyaltyListener, owner: LifecycleOwner) {
        this.listener = listener
        this.owner = owner
        reload()
    }

    /**
     * load()
     */
    fun load() {
        val offset = items.value?.count() ?: 0
        UserApiClient().loyaltyRequests(offset, {
            add(it.requests)
        }) {}
    }

    /**
     * reload()
     */
    fun reload() {
        items.clear()
        UserApiClient().loyaltyRequests(0, {
            add(it.requests)
        }) {}
    }

    /**
     * add
     */
    private fun add(adds: ArrayList<LoyaltyRequestEntity>) {
        val models = adds.map { LoyaltyRequestViewModel(it) }
        items.addAll(models)

        listener.finish()
    }
}