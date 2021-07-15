@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.list.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.GironsEntity
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.parts.BaseViewModelList

/**
 * メイン画面のGironリスト
 */
class GironsViewModel: BaseViewModelList() {
    var word = MutableLiveData("")
    var type = MutableLiveData("")

    /**
     * set
     *
     * @param String type
     * @param String word
     */
    fun set(type: String, word: String): Boolean {
        var reset = false

        if (this.word.value != word || this.type.value != type) reset = true

        this.word.value = word
        this.type.value = type

        if (reset) reset()

        return reset
    }

    /**
     * reset
     */
    fun reset() {
        Log.d(TAG, "reset")

        items.clear()
        search()
    }

    /**
     * search
     */
    fun search() {
        GironApiClient().search(word.value ?: "", type.value ?: "", items.value?.count(), null, {gironsEntity ->
            addItems(gironsEntity)
            maxItem.value = gironsEntity.maxNum
        }, {

        })
    }

    /**
     * addItems
     *
     * @param GironsEntity
     */
    private fun addItems(gironsEntity: GironsEntity) {
        var adds = gironsEntity.girons.map { GironListItemViewModel(it) }
        items.value?.also { value ->
            adds = adds.filter {giron ->
                value.find { it.id == giron.id } == null
            }
        }

        items.addAll(adds)
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "GironsViewModel"
    }
}