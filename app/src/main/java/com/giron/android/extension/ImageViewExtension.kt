package com.giron.android.extension

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.giron.android.util.AuthGlideUrl

@BindingAdapter("loadUserImage")
fun ImageView.loadUserImage(url: String?) {
    url?.also {
        if (it.isEmpty()) return
        if (it.contains("/null/")) return
        Glide.with(this)
                .load(AuthGlideUrl.getUrl(url))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(this)
    }
}

@BindingAdapter("loadResource")
fun ImageView.loadResource(resource: Int) {
    setImageResource(resource)
}

/**
 * setByteArray
 *
 * @param bytes
 */
fun ImageView.setByteArray(bytes:ByteArray){
    val opt = BitmapFactory.Options()
    opt.inJustDecodeBounds = false
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opt)
    setImageBitmap(bitmap)
}