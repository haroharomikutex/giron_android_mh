package com.giron.android.view.giron.detail.viewModel

import com.giron.android.model.dao.RewordObj
import com.giron.android.view.giron.detail.listener.RewordsListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

class RewordViewModel(val reword: RewordObj): BaseViewModel(ViewModelType.Reword) {
    override val id = reword.id
    val coinNum = reword.num.toString()
    var listener: RewordsListener? = null
    val resource = reword.resource
}