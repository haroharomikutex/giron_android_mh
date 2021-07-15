@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.information.viewModel

import androidx.lifecycle.LifecycleOwner
import com.giron.android.model.entity.InformationEntity
import com.giron.android.model.net.InformationApiClient
import com.giron.android.view.information.listener.InformationListener
import com.giron.android.view.parts.BaseViewModelList

/**
 * InformationsViewModel
 */
class InformationsViewModel: BaseViewModelList() {
    lateinit var owner: LifecycleOwner
    private lateinit var listener: InformationListener

    /**
     * set
     *
     * @param LifecycleOwner
     * @param InformationListener
     */
    fun set(owner: LifecycleOwner, listener: InformationListener) {
        this.owner = owner
        this.listener = listener

        reload()
    }

    /**
     * reload
     */
    fun reload() {
        items.clear()
        InformationApiClient().list(0, {
            add(it.information)
        }){}
    }

    /**
     * load
     */
    fun load() {
        val offset = items.count()
        InformationApiClient().list(offset, {
            add(it.information)
        }) {}
    }

    /**
     * add
     *
     * @param ArrayList<InformationEntity>
     */
    private fun add(models: ArrayList<InformationEntity>) {
        val values = models.map{ InformationViewModel(it, listener) }
        items.addAll(values)

        listener.finish()
    }
}