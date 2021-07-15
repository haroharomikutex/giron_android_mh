package com.giron.android.view.giron.detail.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.CommentEntity
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.model.net.HttpConstants
import com.giron.android.util.SHARE_TWITTER
import com.giron.android.util.toDate
import com.giron.android.util.toLabel
import com.giron.android.view.giron.detail.listener.GironDetailListener
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType
import java.net.URLEncoder

class CommentViewModel(val comment: CommentEntity, val listener: GironDetailListener, val giron: GironViewModel? = null) : BaseViewModel(
    ViewModelType.Comment, comment), GironViewModelInterface {
    override val id = comment.id
    val userImageUrl = HttpConstants.urlBase() + "users/${comment.user.uuid}/image_data/"
    val value = comment.comment
    val num = comment.num.toString()
    val numAndName = "${comment.num}: ${comment.user.username}"
    var coinCount: MutableLiveData<String> = MutableLiveData(comment.coinCount.toString())
    private val createAt = comment.createdAt.toDate()
    val time = "・${createAt?.toLabel()}"

    val isSelf: Boolean
        get() {
            val user = UserObjApi().getData()
            user?.also {
                return user.uuid == comment.user.uuid
            }
            return false
        }

    val isFirst: Boolean
        get() {
            if (giron == null) return false
            return giron.uuid == comment.user.uuid
        }

    val hasEmotions = comment.emotions.count() > 0

    /**
     * emotions
     *
     * @return ArrayList<EmotionViewModel>
     */
    fun emotions(): ArrayList<EmotionViewModel> {
        return createEmotions(comment.emotions, comment.IsEmotions)
    }

    /**
     * touchUser
     */
    fun touchUser() {
        listener.touchUser(comment.user.uuid)
    }

    /**
     * twitter
     */
    fun twitter() {
        giron?.let {_giron ->
            val length = if (comment.comment.length > 50) 50 else comment.comment.length
            val tweet = "${comment.comment.substring(0, length)}\n\n#${_giron.giron.title}\n#GIRON\n${HttpConstants.urlBase()}share/giron?giron_id=${_giron.id}"
            val url = "$SHARE_TWITTER${URLEncoder.encode(tweet, "utf-8")}"
            listener.link(url)
        }
    }

    /*
     * ユーザー画像
     */
    companion object {
    }
}