package com.giron.android.view.giron.list.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.view.giron.list.listener.SearchCandidateListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * SearchCandidateViewModel
 *
 * @param String label
 * @param SearchCandidateListener
 */
@Suppress("KDocUnresolvedReference")
class SearchCandidateViewModel(_label: String, val listener: SearchCandidateListener? = null): BaseViewModel(
    ViewModelType.SearchCandidate) {

    val label = MutableLiveData(_label)

    /**
     * click
     */
    fun click() {
        listener?.search(label.value ?: "")
    }
}