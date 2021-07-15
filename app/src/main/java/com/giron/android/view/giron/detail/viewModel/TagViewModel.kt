@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.giron.android.model.entity.TagEditEntity
import com.giron.android.model.entity.TagEntity
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

/**
 * TagViewModel
 * 追加されたタグのViewModel
 *
 * @param TagEntity
 * @param LifecycleOwner
 * @param Boolean Giron主かどうか　タグをロックできるかどうかに関わる
 * @param TagsViewModel 親のViewModel
 */
class TagViewModel(
    override val id: Int,
    val name: String,
    isLock: Boolean,
    owner: LifecycleOwner,
    private val isOwner: Boolean,
    val parent: TagsViewModel? = null
): BaseViewModel(ViewModelType.Tag){

    constructor(tag: TagEntity, owner: LifecycleOwner, isOwner: Boolean, parent: TagsViewModel?) :
            this(tag.id, tag.name, tag.isLock, owner, isOwner, parent)
    constructor(tagObj: TagEditEntity, owner: LifecycleOwner, parent: TagsViewModel?) :
            this(0, tagObj.name, tagObj.isLock, owner, true, parent)

    val text = "#$name"
    var isLock: MutableLiveData<Boolean> = MutableLiveData(isLock)
    var isLockOnOwner: MutableLiveData<Boolean> = MutableLiveData(isOwner && isLock)
    val isLockOn: MutableLiveData<Boolean> = MutableLiveData(!isOwner && isLock)
    val isLockOff: MutableLiveData<Boolean> = MutableLiveData(isOwner && !isLock)
    val isRemove: MutableLiveData<Boolean> = MutableLiveData(!isLock)

    /**
     * init
     */
    init {
        this.isLock.observe(owner, Observer {
            isLockOnOwner.value = isOwner && it
            isLockOn.value = !isOwner && it
            isLockOff.value = isOwner && !it
            isRemove.value = !it
        })
    }

    /**
     * lockOn
     */
    fun lockOn() {
        isLock.value = false
    }

    /**
     * lockOff
     */
    fun lockOff() {
        isLock.value = true
    }

    /**
     * remove
     */
    fun remove() {
        parent?.remove(this)
    }

    /**
     * static
     */
    companion object{
        private const val TAG = "TagViewModel"
    }
}
