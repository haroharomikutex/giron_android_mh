@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.information.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.InformationEntity
import com.giron.android.util.toDate
import com.giron.android.util.toLabel
import com.giron.android.view.information.listener.InformationListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * InformationViewModel
 *
 * @param InformationEntity
 * @param InformationListener
 */
class InformationViewModel(override val entity: InformationEntity, val listener: InformationListener): BaseViewModel(
    ViewModelType.Information) {
    private val type = InformationType.fromInt(entity.type)
    private val gironId = entity.giron?.id ?: 0
    private val commentNum = entity.comment?.num ?: 0
    private val url = entity.url
    val dateStr = MutableLiveData(entity.date.toDate()?.toLabel())
    val title = MutableLiveData(entity.title)
    val message = MutableLiveData(entity.message)

    /**
     * click
     */
    fun click() {
        when(type) {
            InformationType.Giron, InformationType.Comment -> listener.touchGiron(gironId, commentNum)
            InformationType.Url -> listener.touchUrl(url)
            InformationType.RequiredUrl -> listener.touchUrl(url, true)
            else -> return
        }
    }
}