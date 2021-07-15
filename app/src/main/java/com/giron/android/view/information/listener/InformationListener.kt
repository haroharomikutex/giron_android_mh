package com.giron.android.view.information.listener

interface InformationListener {
    fun touchGiron(id: Int, num: Int? = null)
    fun touchUrl(url: String, required: Boolean = false)
    fun touchTag(tag: String)
    fun finish()
}