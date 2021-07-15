@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.entity

import com.giron.android.view.giron.detail.viewModel.TagViewModel
import com.google.gson.annotations.SerializedName

/**
 * TagEditEntity
 */
data class TagEditEntity (
        val name: String,
        val order: Int,
        @SerializedName("is_lock")
        val isLock: Boolean,
        @SerializedName("girontag_id")
        val gironTagId: Int
) {
    /**
     * static
     */
    companion object {
        /**
         * create
         *
         * @param TagViewModel
         * @param Int order
         */
        fun create(tag: TagViewModel, order: Int) : TagEditEntity {
            return TagEditEntity(name = tag.name, order = order, isLock = tag.isLock.value ?: false, gironTagId = tag.id)
        }
    }
}