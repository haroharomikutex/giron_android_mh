@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.list.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.view.parts.BaseViewModelList

/**
 * Giron一覧の全体を司る
 */
class GironTopViewModel(word: String, isCreatable: Boolean = true): BaseViewModelList() {
    var word = MutableLiveData(word)
    var isCreatable = MutableLiveData(isCreatable)

    /**
     * set
     *
     * @param String
     */
    fun set(word: String) {
        this.word.value = word
    }
}
