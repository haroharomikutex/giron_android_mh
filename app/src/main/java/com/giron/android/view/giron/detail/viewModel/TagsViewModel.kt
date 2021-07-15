@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.viewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.giron.android.R
import com.giron.android.extension.showAlertDialog
import com.giron.android.model.entity.TagEntity
import com.giron.android.model.net.realm.TagObjApi
import com.giron.android.model.net.GironApiClient
import com.giron.android.util.MAX_TAG_COUNT
import com.giron.android.util.TAG_PATTERN_SINGLE
import com.giron.android.view.parts.BaseViewModelList
import java.util.regex.Pattern

/**
 * TagsViewModel
 *
 * @param Int? GironID(新規の場合はnull)
 * @param LifecycleOwner
 * @param Boolean Giron主か
 */
open class TagsViewModel : BaseViewModelList() {
    private var id: Int? = null
    lateinit var owner: LifecycleOwner
    protected open var isOwner = false
    var printCount = MutableLiveData("")
    var inputText = MutableLiveData("")
    var count = MutableLiveData(false)
    val recentlyTag = TagObjApi().getRecentData().joinToString(" ") { it.toString() }
    val officialTag = tagsString(TagObjApi.TagObjType.Official)
    val recommendTag = tagsString(TagObjApi.TagObjType.Recommend)
    val hosTag = tagsString(TagObjApi.TagObjType.Hot)
    var isEdited = false

    // 追加したタグを記録しておき、保存時にRealmに書き込む
    var addsTag = mutableListOf<String>()

    val tags: List<TagViewModel>
        get() {
            items.value?.let {
                return it.map { tag ->
                    tag as TagViewModel
                }
            }
            return ArrayList()
        }

    fun set(id: Int?, owner: LifecycleOwner, isOwner: Boolean) {
        this.id = id
        this.owner = owner
        this.isOwner = isOwner

        // アイテムリストが更新された場合
        // タグの数を更新する
        items.observe(owner, Observer {
            count.value = items.count() > 0
            printCount.value = "${items.count()}/$MAX_TAG_COUNT"
            isEdited = true
        })
    }

    /**
     * add
     * タグを追加する
     *
     * @param String タグ名
     * @param Context? アラートの出力に必要
     */
    fun add(name: String, context: Context?): Boolean {
        if (name.isEmpty()) {
            return false
        }

        // 適切なタグ名か
        if (!isProper(name)) {
            context?.let {
                it.showAlertDialog(it.getString(R.string.tag_is_error))
            }
            return false
        }

        // 既に入力されているタグ
        if (checkExistTag(name)) {
            return true
        }

        // タグが最大限まで入力されている場合は何もしない
        if (isMax()) {
            context?.let {
                it.showAlertDialog(it.getString(R.string.tagcount_is_over))
                return false
            }
        }

        val tagEntity = TagEntity(id = -1, name = name, isLock = false)
        val tag = TagViewModel(tagEntity, owner, isOwner, this)
        items.add(tag)
        addsTag.add(name)
        return true
    }

    /**
     * remove
     * タグを削除する
     *
     * @param TagViewModel
     */
    fun remove(tag: TagViewModel) {
        items.remove(tag)
    }

    /**
     * isMax
     * タグの個数が最大値に達しているか
     *
     * @return Boolean
     */
    private fun isMax(): Boolean {
        return items.count() >= MAX_TAG_COUNT
    }

    /**
     * checkExistTag
     * 既にタグが存在しているか？
     *
     * @param String タグ名
     *
     * @return Boolean
     */
    private fun checkExistTag(name: String): Boolean {
        items.value?.let {list ->
            return list.count {
                if (it is TagViewModel) {
                    return@count it.name == name
                }
                return@count false
            } > 0
        }
        return false
    }

    /**
     * isProper
     * タグが適切な文字列かチェックする
     *
     * @param String タグ名
     *
     * @return Boolean 適切か？
     */
    private fun isProper(name: String): Boolean {
        val regex = Pattern.compile(TAG_PATTERN_SINGLE)
        val matcher = regex.matcher(name)

        while (matcher.find()) {
            return true
        }

        return false
    }

    /**
     * getData
     * このGironのタグを取得する
     */
    fun getData() {
        items.clear()

        id?.let { _id ->
            Log.d(TAG, "get tagdata")
            GironApiClient().tags(_id, {tags ->
                val adds = tags.tags.map {
                    TagViewModel(it, owner, isOwner, this)
                }
                items.addAll(adds)
                isEdited = false
            }, {})
        }
    }

    /**
     * tags
     * 指定したタイプのタグを取得する
     *
     * @param TagObjApi.TagObjType タグタイプ
     *
     * @return String Ex:"#aaa #bbb..."
     */
    private fun tagsString(type: TagObjApi.TagObjType): String {
        return TagObjApi().getData(type).joinToString(" "){ it.toString() }
    }
    /**
     * static
     */
    companion object {
        private const val TAG = "TagsViewModel"
    }
}