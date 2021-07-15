@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.mypage.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * GironTopFactory
 *
 * @param LifecycleOwner
 */
class MyPageFactory(private val owner: LifecycleOwner) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyPageViewModel(owner) as T
    }
}