@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * GironTopFactory
 *
 * @param String word
 */
class GironTopFactory(private val word: String, private val isCreatable: Boolean) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GironTopViewModel(word, isCreatable) as T
    }
}