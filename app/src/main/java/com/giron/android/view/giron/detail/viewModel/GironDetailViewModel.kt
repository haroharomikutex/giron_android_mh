package com.giron.android.view.giron.detail.viewModel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.CommentEntity
import com.giron.android.model.entity.GironDetailEntity
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.detail.listener.GironDetailListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.BaseViewModelList
import com.giron.android.view.parts.ViewModelType

/**
 * GironDetailViewModel
 * Giron詳細画面全体
 */
class GironDetailViewModel: BaseViewModelList() {
    private lateinit var owner: LifecycleOwner
    private lateinit var listener: GironDetailListener
    private var id = 0
    private var num: Int? = null
    lateinit var giron: GironDetailEntity
    lateinit var gironViewModel: GironViewModel
    var title = ""
    private var _scrollNum = MutableLiveData(0)
    private lateinit var comments: ArrayList<CommentEntity>

    var comment = MutableLiveData("")

    /**
     * isFirst
     */
    val isFirst: Boolean
        get() {
            UserObjApi().getData()?.let {
                return it.uuid == giron.user.uuid
            }
            return false
        }

    var scrollNum: Int
        get() = _scrollNum.value ?: 0
        set(value) {
            _scrollNum.value = value
        }

    /**
     * set
     *
     * @param id
     * @param num
     * @param owner
     * @param listener
     */
    fun set(id: Int, num: Int = 0, owner: LifecycleOwner, listener: GironDetailListener): Boolean {
        var refresh = false

        if (this.id != id || this.num != num) refresh = true

        this.id = id
        this.num = num
        this.owner = owner
        this.listener = listener

        if (items.value == null) refresh = true
        items.value?.let {
            if (it.count() == 0) refresh = true
        }

        Log.d(TAG, "refresh flag = $refresh")
        if (refresh) {
            scrollNum = num
            retrieve()
        }

        return refresh
    }

    /**
     * reset()
     */
    fun reset() {
        scrollNum = 0
        retrieve()
    }

    /**
     * isLastPage
     *
     * @return Boolean
     */
    fun isLastPage(): Boolean {
        return giron.maxPage == giron.currentPage
    }

    /**
     * onWrite
     */
    fun onWrite() {
        listener.openCommentWrite(giron.id)
    }

    /**
     * new_comment
     */
    fun newComment() {
        val max = comments.maxByOrNull { it.num }?.num ?: 0
        val count = comments.count()
        Log.d(TAG, "new_comment offset = $max")

        GironApiClient().newComment(id, max, { commentsEntity ->
            comments.addAll(commentsEntity.comments)
            val adds = commentsEntity.comments.map { CommentViewModel(it, listener, gironViewModel) }
            items.addAll(adds)
            listener.insert(count + 1, commentsEntity.comments.count())
        }, {})
    }

    /**
     * retrieve
     */
    private fun retrieve() {
        Log.d(TAG, "retrieve Giron id=$id")
        items.clear()

        GironApiClient().retrieve(id, num, { gironEntity ->
            giron = gironEntity
            comments = giron.comments
            addItems()
            title = gironEntity.title
            listener.setTitle(gironEntity.title)
            listener.reload()
        }, {})
    }

    /**
     * ページ指定更新
     *
     * @param Int ページ番号
     */
    fun getCommentByPage(page: Int) {
        GironApiClient().commentsByPage(id, page, {commentsEntity ->
            comments = commentsEntity.comments
            giron.maxNum = commentsEntity.maxNum
            giron.maxPage = commentsEntity.maxPage
            giron.currentPage = commentsEntity.currentPage
            addItems()
            listener.reload()
        }, {})
    }

    /**
     * ページ指定更新
     *
     * @param num ページ番号
     */
    fun getCommentByNum(num: Int) {
        GironApiClient().commentsByNum(id, num, {commentsEntity ->
            comments = commentsEntity.comments
            giron.maxNum = commentsEntity.maxNum
            giron.maxPage = commentsEntity.maxPage
            giron.currentPage = commentsEntity.currentPage
            addItems()
            listener.reload()
        }, {})
    }

    /**
     * addItems
     * コメント再描画
     */
    private fun addItems() {
        items.clear()

        gironViewModel = GironViewModel(giron, owner, listener)
        val adds = arrayListOf(gironViewModel as BaseViewModel)

        // 矢印
        val arrows = ArrowsViewModel(this)
        if (comments.count() > 0 && giron.maxPage >= 1) { adds.add(arrows) }

        comments.forEach{
            adds.add(CommentViewModel(it, listener, gironViewModel))
        }

        // 最後はコメントが長ければ追加する
        if (comments.count() > 5 && giron.maxPage >= 1) { adds.add(arrows) }

        items.addAll(adds)
    }

    /**
     * getPosByNum
     * コメントナンバーから位置を取得する
     *
     * @param Int Check Comment Number
     */
    fun getPosByNum(num: Int): Int? {
        if (num <= 0) return null

        items.value?.let { items ->
            items.forEachIndexed{ i, model ->
                if (model.modelType != ViewModelType.Comment) return@forEachIndexed

                if (model is CommentViewModel)
                    if (model.comment.num == num) return i
            }
        }

        return null
    }

    /**
     * hasNum
     *
     * @param Int Check existance Comment Number
     */
    fun hasNum(num: Int): Boolean {
        if (num <= 0) return false

        items.value?.let { items ->
            items.forEach{ model ->
                if (model.modelType != ViewModelType.Comment) return@forEach

                if (model is CommentViewModel)
                    if (model.comment.num == num) return true
            }
        }

        return false
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "GironDetailViewModel"
    }
}