@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.create.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.giron.android.model.net.realm.CreateGironObjApi
import com.giron.android.view.giron.detail.viewModel.TagViewModel
import com.giron.android.view.giron.detail.viewModel.TagsViewModel

/**
 * CreateGironViewModel
 */
class CreateGironViewModel : TagsViewModel() {
    var title = MutableLiveData("")
    var description = MutableLiveData("")
    override var isOwner = true

    /**
     * set
     *
     * @param LifecycleOwner
     */
    fun set(owner: LifecycleOwner) {
        super.set(0, owner, true)

        items.clear()

        // set from Realm
        val createGironObj = CreateGironObjApi().getData()
        title.value = createGironObj.title
        description.value = createGironObj.description

        createGironObj.tags.forEach { tagObj ->
            items.add(
                TagViewModel(
                    tagObj,
                    owner,
                    this
                )
            )
        }
    }
}