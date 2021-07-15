package com.giron.android.view.giron.list.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.view.giron.list.listener.SearchCandidateListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * SearchCandidateHeaderViewModel
 *
 * @param String label
 * @param SearchCandidateListener
 * @param SearchCandidateHeaderType
 */
@Suppress("KDocUnresolvedReference")
class SearchCandidateHeaderViewModel(
    _label: String,
    val listener: SearchCandidateListener, private
    val type: SearchCandidateHeaderType = SearchCandidateHeaderType.None
): BaseViewModel(ViewModelType.SearchCandidateHeader) {
    val label = MutableLiveData(_label)

    /**
     * click
     */
    fun click() {
        if (type == SearchCandidateHeaderType.Clear) {
            listener.search("")
        }
    }
}