@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.loyalty.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.extension.locate
import com.giron.android.model.entity.LoyaltyRequestEntity
import com.giron.android.util.toDate
import com.giron.android.util.toLabel
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * LoyaltyRequestViewModel
 *
 * @param LoyaltyRequestEntity
 */
class LoyaltyRequestViewModel(override val entity: LoyaltyRequestEntity): BaseViewModel(
    ViewModelType.LoyaltyRequestHistory) {
    val num = MutableLiveData("${entity.num}円分")
    private val type = LoyaltyType.fromString(entity.type)
    val typeStr = MutableLiveData(type.value.locate())
    private val state = LoyaltyStateType.fromInt(entity.state)
    val stateStr = MutableLiveData(state.toString())
    val createdAt = MutableLiveData(entity.createdAt.toDate()?.toLabel())
}