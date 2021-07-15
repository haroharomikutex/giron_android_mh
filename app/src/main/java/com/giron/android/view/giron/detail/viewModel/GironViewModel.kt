@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.viewModel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.giron.android.model.entity.GironDetailEntity
import com.giron.android.model.net.HttpConstants
import com.giron.android.util.*
import com.giron.android.view.giron.detail.listener.GironDetailListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType
import java.net.URLEncoder

/**
 * GironViewModel
 * Giron詳細の頭の情報
 *
 * @param GironDetailEntity
 * @param GironDetailListener
 */
class GironViewModel(var giron: GironDetailEntity, owner: LifecycleOwner, var listener: GironDetailListener? = null) : BaseViewModel(
    ViewModelType.Giron, giron), GironViewModelInterface {
    override val id = giron.id
    private val title = giron.title
    val listTitle = "#${giron.id} $title"
    val description = giron.description
    private val createAt = giron.createdAt.toDate()
    var coinCount: MutableLiveData<String> = MutableLiveData(giron.allCoinCount.toString())
    val userImageUrl = HttpConstants.urlBase() + "users/${giron.user.uuid}/image_data/"
    val uuid = giron.user.uuid
    val tags = MutableLiveData(giron.tags)
    var tagString = MutableLiveData("")
    val hasTag = MutableLiveData(false)

    /**
     * init
     */
    init {
        tags.observe(owner, Observer {tagEntity ->
            Log.d(TAG, "tags updated")
            tagString.value = tagEntity.map {it.toString()}.plus("＋").joinToString("  ")
            hasTag.value = tagEntity.count() > 0
        })
    }

    /**
     * clickReword
     */
    fun clickReword() {
        listener?.openReword(giron.id, callback = { coin ->
            coinCount.value = coin.toString()
        })
    }

    /**
     * emotions
     *
     * @return ArrayList<EmotionViewModel> エモーション
     */
    fun emotions(): ArrayList<EmotionViewModel> {
        return createEmotions(giron.emotions, giron.IsEmotions)
    }

    /**
     * detailNameLabel
     *
     * @return String
     */
    fun detailNameLabel(): String {
        val username = giron.user.username
        val views = giron.viewCount

        createAt?.let {
            val createdDateStr = createAt.toLabel()
            return "$username・${views}Views・${giron.commentCount}Comments・$createdDateStr"
        }
        return "$username・${views}Views"
    }

    /**
     * addTag
     */
    fun addTag() {
        Log.d(TAG, "touch add Tag")
        listener?.openTagEdit(giron.id) { entity ->
            Log.d(TAG, "update tags ${entity.tags}")
            tags.value = entity.tags
            tagString.value = entity.tags.map {it.toString()}.plus("＋").joinToString("  ")
            hasTag.value = entity.tags.count() > 0
        }
    }

    /**
     * touchUser
     */
    fun touchUser() {
        listener?.touchUser(giron.user.uuid)
    }

    /**
     * twitter
     */
    fun twitter() {
        val tweet = "${giron.title}\n\n#GIRON\n${HttpConstants.urlBase()}share/giron?giron_id=${giron.id}"
        val url = "$SHARE_TWITTER${URLEncoder.encode(tweet, "utf-8")}"
        listener?.link(url)
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "GironViewModel"
    }
}