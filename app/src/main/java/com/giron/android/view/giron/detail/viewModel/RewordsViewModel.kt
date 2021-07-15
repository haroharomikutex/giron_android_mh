package com.giron.android.view.giron.detail.viewModel

import android.util.Log
import com.giron.android.model.net.realm.RewordObjApi
import com.giron.android.view.giron.detail.listener.RewordsListener
import com.giron.android.view.parts.BaseViewModelList

class RewordsViewModel(val listener: RewordsListener): BaseViewModelList() {
    init {
        getData()
    }

    private fun getData() {
        items.clear()

        Log.d(TAG, "get reword data from Realm")

        RewordObjApi().getData().forEach {
            it.listener = listener
            items.add(it)
        }
    }

    companion object {
        private const val TAG = "RewordsViewModel"
    }
}