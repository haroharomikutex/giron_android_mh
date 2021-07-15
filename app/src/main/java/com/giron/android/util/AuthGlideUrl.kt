package com.giron.android.util

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.giron.android.model.dao.KeyValueObj
import com.giron.android.model.net.realm.KeyValueObjApi
import com.giron.android.model.net.HttpConstants

class AuthGlideUrl {
    companion object {
        fun getUrl(url: String) : GlideUrl {
            val tokenObj = KeyValueObjApi().getValue(KeyValueObj.Key.GIRON_TOKEN) ?: ""
            return GlideUrl(url, LazyHeaders.Builder()
                    .addHeader(HttpConstants.HEADER_AUTH, tokenObj)
                    .build())
        }
    }
}