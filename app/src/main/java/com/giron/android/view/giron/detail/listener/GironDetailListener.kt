package com.giron.android.view.giron.detail.listener

import android.view.View
import com.giron.android.model.entity.EmotionsEntity
import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.TagsEntity

interface GironDetailListener {
    fun touchTag(tag: String) {}
    fun touchGiron(id: Int, num: Int? = null, byNum: Int? = null) {}
    fun touchComment(num: Int, byNum: Int? = null) {}
    fun touchUser(uuid: String){}
    fun setTitle(title: String) {}
    fun openEmotionListGiron(id: Int, view: View, callback: ((entity: EmotionsEntity)-> Unit)) {}
    fun openEmotionListComment(id: Int, num: Int, view: View, callback: ((entity: EmotionsEntity)-> Unit)) {}
    fun openReword(id: Int, num: Int? = null, callback: (coin: Int) -> Unit) {}
    fun openAction(id: Int, rewordCallback: ((coin: Int) -> Unit), emotionCallback: ((entity: EmotionsEntity)-> Unit), copyCallback: (()->Unit), replyCallback: (()->Unit))
    fun openTagEdit(id: Int, callback: ((entity: TagsEntity) -> Unit))
    fun write(message: String)
    fun reload()
    fun insert(pos: Int, count: Int)
    fun openCommentWrite(id: Int)
    fun link(url: String)
    fun showError(error: ErrorResponse)
    fun copyClipBoard(string: String)
}