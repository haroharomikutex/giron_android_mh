package com.giron.android.view.giron.detail.viewModel

import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

class TagCandidateViewModel(private val name: String): BaseViewModel(ViewModelType.TagCandidate) {
    override fun toString(): String {
        return "#$name"
    }
}