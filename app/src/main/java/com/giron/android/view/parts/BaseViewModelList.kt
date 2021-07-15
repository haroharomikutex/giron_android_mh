package com.giron.android.view.parts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giron.android.util.MutableListLiveData

/**
 * Gironリストのベースモデル
 */
open class BaseViewModelList: ViewModel() {
    var items: MutableListLiveData<BaseViewModel> = MutableListLiveData()
    var maxItem : MutableLiveData<Int> = MutableLiveData()
}