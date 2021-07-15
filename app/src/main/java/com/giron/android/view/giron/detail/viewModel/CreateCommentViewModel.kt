@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.net.realm.WritingCommentObjApi
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * CreateCommentViewModel
 */
class CreateCommentViewModel : BaseViewModel(ViewModelType.WriteComment) {
    override var id = 0
    var comment = MutableLiveData("")

    /**
     * setComment
     *
     * @return String
     */
    fun setComment() {
        comment.value = WritingCommentObjApi().getData(id)
    }
}