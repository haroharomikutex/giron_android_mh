package com.giron.android.model.net.realm

import com.giron.android.model.dao.TagObj
import com.giron.android.model.net.TagApiClient
import com.giron.android.view.giron.detail.viewModel.TagCandidateViewModel
import io.realm.Realm
import io.realm.Sort

/**
 * 最近追加したタグ
 */
class TagObjApi: RealmObjApi() {
    enum class TagObjType(val value: String) {
        Recently("recently"),
        Hot("hot"),
        Official("official"),
        Recommend("recommend");
    }

    /**
     * static
     */
    companion object {
        private const val LIMIT: Long = 10
        private const val TAG = "TagObjApi"
    }

    /**
     * @return 最近追加されたタグのリストを10件取得
     */
    fun getData(type: TagObjType) : List<TagCandidateViewModel> {
        // get Realm instance
        val realm = Realm.getDefaultInstance()

        val data = realm
                .where(TagObj::class.java)
                .equalTo("type", type.value)
                .sort("createdAt", Sort.ASCENDING)
                .limit(LIMIT)
                .findAll()

        // ViewModelに変換
        val result = data.map{
            TagCandidateViewModel(it.name)
        }

        // close Realm
        realm.close()

        return result
    }

    /**
     * getRecentData
     * 最近追加したタグを取得
     */
    fun getRecentData(): List<TagCandidateViewModel> {
        // get Realm instance
        val realm = Realm.getDefaultInstance()

        val data = realm
                .where(TagObj::class.java)
                .equalTo("type", TagObjType.Recently.value)
                .sort("createdAt", Sort.DESCENDING)
                .limit(LIMIT)
                .findAll()

        // ViewModelに変換
        val result = data.map{
            TagCandidateViewModel(it.name)
        }

        // close Realm
        realm.close()

        return result
    }

    /**
     * よく追加されているタグ
     * オフィシャルタグ
     * おすすめタグを取得して保存する
     */
    fun setData() {
        // get Realm instance
        val realm = Realm.getDefaultInstance()

        // hot
        TagApiClient().hot({
            realm.executeTransaction { r ->
                it.hot.forEach{name ->
                    val tag = TagObj()
                    tag.set(name, TagObjType.Hot)
                    r.insertOrUpdate(tag)
                }
                it.recommend.forEach{name ->
                    val tag = TagObj()
                    tag.set(name, TagObjType.Recommend)
                    r.insertOrUpdate(tag)
                }
                it.official.forEach{name ->
                    val tag = TagObj()
                    tag.set(name, TagObjType.Official)
                    r.insertOrUpdate(tag)
                }
            }

            // close Realm
            realm.close()
        }, {})
    }

    /**
     * setRecentlyData
     * Record recently added tags
     *
     * @param String Tag name
     */
    fun setRecentlyData(name: String) {
        // get Realm instance
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction { r ->
            val tag = TagObj()
            tag.set(name, TagObjType.Recently)
            r.insertOrUpdate(tag)
        }

        // close Realm
        realm.close()
    }
}