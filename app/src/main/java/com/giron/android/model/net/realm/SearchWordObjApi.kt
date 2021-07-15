package com.giron.android.model.net.realm

import com.giron.android.model.dao.SearchWordObj
import com.giron.android.view.giron.list.listener.SearchCandidateListener
import com.giron.android.view.giron.list.viewModel.SearchCandidateViewModel
import io.realm.Realm
import io.realm.Sort
import java.util.*

/**
 * SearchWordObjApi
 * 最近検索したワード
 */
class SearchWordObjApi: RealmObjApi() {
    /**
     * getData
     *
     * @return List<SearchCandidateViewModel>
     */
    fun getData(listener: SearchCandidateListener? = null): List<SearchCandidateViewModel> {
        val realm = Realm.getDefaultInstance()

        val data = realm
                .where(SearchWordObj::class.java)
                .sort("createdAt", Sort.ASCENDING)
                .limit(LIMIT)
                .findAll()

        // ViewModelに変換
        val result = data.map {
            SearchCandidateViewModel(it.word, listener)
        }

        // close Realm
        realm.close()

        return result
    }

    fun getData(word: String): List<String> {
        val realm = Realm.getDefaultInstance()

        val data = realm
                .where(SearchWordObj::class.java)
                .beginsWith("word", word)
                .sort("createdAt", Sort.ASCENDING)
                .limit(LIMIT)
                .findAll()

        // ViewModelに変換
        val result = data.map {
            it.word
        }

        // close Realm
        realm.close()

        return result
    }

    /**
     * setData
     *
     * @param String
     */
    fun setData(word: String) {
        if (word.isEmpty()) return

        // get Realm instance
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            val searchWordObj = SearchWordObj()
            searchWordObj.word = word
            searchWordObj.createdAt = Date()
            it.insertOrUpdate(searchWordObj)
        }

        // close Realm
        realm.close()
    }

    /**
     * static
     */
    companion object {
        private const val LIMIT: Long = 10
        private const val TAG = "SearchWordObjApi"
    }
}