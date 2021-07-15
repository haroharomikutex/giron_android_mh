package com.giron.android.view.giron.detail.listener

import android.content.Context

interface TagEditListener {
    fun touchTag(name: String)
    fun listenerContext(): Context
}