package com.giron.android.view.profile.listener


interface UserProfileListener {
    fun touchTag(tag: String) {}
    fun touchGiron(id: Int, num: Int? = null, byNum: Int? = null) {}
}