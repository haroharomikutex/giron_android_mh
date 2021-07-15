package com.giron.android.view.parts

import androidx.lifecycle.*
import com.giron.android.model.entity.EmotionsEntity
import com.giron.android.model.entity.MyProfileResponseEntity
import com.giron.android.model.entity.TagsEntity

object ShareViewModelStore {
    private val _store = ViewModelStore()
    val store = _store
}

interface SharedViewModelStoreOwner : ViewModelStoreOwner {
    override fun getViewModelStore(): ViewModelStore = ShareViewModelStore.store
}

/**
 * ShareViewModel
 * Activity, Fragment で共有するViewModel
 */
class ShareViewModel : ViewModel() {
    var addEmotionByFragment: ((entity: EmotionsEntity) -> Unit)? = null
    var rewordByFragment: ((coin: Int) -> Unit)? = null
    var tagEditByFragment: ((tagsEntity: TagsEntity) -> Unit)? = null
    var writeCommentByFragment: ((num: Int) -> Unit)? = null
    var searchByFragment = arrayListOf<((word: String) -> Unit)>()
    var editProfileByFragment: ((user: MyProfileResponseEntity) -> Unit)? = null
    var setSearchWordByFragment: ((word: String) -> Unit)? = null
    var replyByFragment: (() -> Unit)? = null
    var copyByFragment: (() -> Unit)? = null

    companion object {
        fun get(
                viewModelStoreOwner: SharedViewModelStoreOwner
        ): ShareViewModel = ViewModelProvider(viewModelStoreOwner, ViewModelProvider.NewInstanceFactory()).get(
            ShareViewModel::class.java)
    }
}