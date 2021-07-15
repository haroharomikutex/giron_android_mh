@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.list.viewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.giron.android.R
import com.giron.android.model.net.realm.SearchWordObjApi
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.list.listener.SearchCandidateListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList

/**
 * SearchCandidatesViewModel
 * 検索候補画面ViewModel
 */
class SearchCandidatesViewModel: BaseViewModelList() {
    private lateinit var listener: SearchCandidateListener
    private lateinit var owner: LifecycleOwner
    private lateinit var context: Context
    private lateinit var populationWords: List<BaseViewModel>

    /**
     * set
     *
     * @param LifecycleOwner
     */
    fun set(owner: LifecycleOwner, context: Context, listener: SearchCandidateListener) {
        this.owner = owner
        this.context = context
        this.listener = listener

        getData()
    }

    /**
     * getData
     */
    private fun getData() {
        GironApiClient().getPopularityWord("", {
            val models = it.words.map { word ->
                SearchCandidateViewModel(word.word, listener)
            }

            populationWords = models

            // アイテム更新
            resetItems()
        }, {})
    }

    /**
     * setWord
     * 検索候補文字列から検索候補取得
     *
     * @param String
     */
    fun setWord(word: String) {
        if (word.isEmpty()) {
            resetItems()
            return
        }

        GironApiClient().searchCandidate(word, {entity ->
            val words = entity.words.map { word ->
                word.word
            }.toMutableList()

            words.addAll(SearchWordObjApi().getData(word))

            val models = words.distinct().map { SearchCandidateViewModel(it, listener) }

            resetItems(models)
        }, {})
    }

    /**
     * resetItems
     *
     * @param List<BaseViewModel>?
     */
    private fun resetItems(_models: List<BaseViewModel>? = null) {
        items.clear()

        _models?.let {
            items.addAll(it)
        } ?: run {
            // 全件検索
            val models = mutableListOf(SearchCandidateHeaderViewModel(context.getString(R.string.all_search), listener, SearchCandidateHeaderType.Clear) as BaseViewModel)

            // 最近の検索結果
            models.add(SearchCandidateHeaderViewModel(context.getString(R.string.search_history), listener))
            SearchWordObjApi().getData(listener).forEach {
                models.add(it)
            }

            // 人気の検索結果
            models.add(SearchCandidateHeaderViewModel(context.getString(R.string.popular_word), listener))
            models.addAll(populationWords)
            items.addAll(models)
        }
    }
}