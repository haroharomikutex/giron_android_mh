package com.giron.android.view.notifications.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.NoticeEntity
import com.giron.android.model.net.HttpConstants
import com.giron.android.util.toDate
import com.giron.android.util.toLabel
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.giron.detail.viewModel.EmotionViewModel
import com.giron.android.view.parts.ViewModelType


/**
 * NoticeViewModel
 */
class NoticeViewModel(override val entity: NoticeEntity): BaseViewModel(ViewModelType.Notice, entity) {
    private val notice = entity.notice
    private val date = entity.createdAt.toDate()
    val imageUserUrl = HttpConstants.urlBase() + "users/${entity.noticer?.uuid}/image_data/"
    val noticeType = NoticeType.fromInt(entity.noticeType)
    val showUserImage = entity.noticer != null
    val showGiron = entity.giron != null
    val showComment = entity.comment != null
    val showIcon = noticeType != NoticeType.EMOTION
    val showEmotion = noticeType == NoticeType.EMOTION
    var iconStr = MutableLiveData("")
    var noticeStr = MutableLiveData("")
    var gironTitle = MutableLiveData("")
    var commentValue = MutableLiveData("")
    var iconResource = MutableLiveData(0)
    val time = "${date?.toLabel()}"

    init {
        if (showIcon) {
            // set IconStr
            entity.emotion?.let {
                val emotion = EmotionViewModel(it)
                iconStr.value = emotion.type.icon
            }
        }

        when(noticeType) {
            else -> {
                noticeStr.value = entity.notice
            }
        }

        if (showGiron) {
            entity.giron?.let {
                gironTitle.value = it.title
            }
        }

        if (showComment) {
            entity.comment?.let {
                commentValue.value = it.comment
            }
        }

        if (showIcon) {
            iconResource.value = noticeType.toNoticeIcon()
        }

        if (showEmotion) {
            entity.emotion?.let {
                val type = EmotionViewModel(it)
                iconStr.value = type.type.icon
            }
        }
    }
}